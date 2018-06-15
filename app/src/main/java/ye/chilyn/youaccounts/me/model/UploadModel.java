package ye.chilyn.youaccounts.me.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.base.BaseModel;
import ye.chilyn.youaccounts.constant.HandleModelType;
import ye.chilyn.youaccounts.constant.RefreshViewType;
import ye.chilyn.youaccounts.util.LG;

/**
 * Created by Alex on 2018/6/6.
 */

public class UploadModel extends BaseModel{

    public static final String TAG = "UploadModel>>>";
    private static final String HOST = "45.77.169.213";
    private static final int SERVER_PORT = 6666;
    private static final String START_UPLOAD = "start upload";
    private static final String FINISHED = "finished";
    private static final String OK = "ok";
    private static final String TERMINATOR = "-------END-------";
    private static final int SEARCH_HOST_TIME = 51;
    private ExecutorService mSearchHostExecutor;
    private ExecutorService mUploadFileExecutor;
    private ScheduledExecutorService mTimerExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;
    private String mIPAddress;
    private int mSearhHostFinishedCount = 0;
    private long mUploadingLength;
    private long mTotalLength;

    public UploadModel(OnRefreshViewListener listener) {
        super(listener);
    }

    @Override
    public void handleModelEvent(int type, Object data) {
        switch (type) {
            case HandleModelType.UPLOAD_TO_LOCAL_SERVER:
                uploadToLocalServer();
                break;

            case HandleModelType.UPLOAD_TO_REMOTE_SERVER:
                uploadToRemoteServer();
                break;

            case HandleModelType.CANCEL_UPLOAD:
                cancelUpload();
                break;
        }
    }

    public void cancelUpload() {
        shutdownSearchHost();
        shutdownUploadExcutor();
        log("cancel upload");
    }

    /**
     * 上传至本地服务器
     */
    public void uploadToLocalServer() {
        mIPAddress = getDeviceIp();
        if (TextUtils.isEmpty(mIPAddress)) {
            return;
        }

        initSearchHostExecutor();
        searchServerHost();
    }

    /**
     * 获取手机Wifi的IP地址
     * @return
     */
    private String getDeviceIp(){
        @SuppressLint("WifiManagerLeak") WifiManager wm = (WifiManager) AccountsApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
        if(wm == null) {
            callRefreshView(RefreshViewType.UPLOAD_FAILED, "无WIFI设备信息");
            return "";
        }

        //检查Wifi状态
        if(!wm.isWifiEnabled()) {
            callRefreshView(RefreshViewType.UPLOAD_FAILED, "WIFI未打开");
            return "";
        }

        WifiInfo wi = wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.”地址
        return changeIpToString(ipAdd);
    }

    /**
     * 把整型地址转换成“*.*.*.”地址
     * @param ipAddress IP整型地址
     * @return
     */
    private String changeIpToString(int ipAddress) {
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8 ) & 0xFF) + "." +
                ((ipAddress >> 16 ) & 0xFF) + ".";
    }

    /**
     * 初始化搜索服务器的线程池
     */
    private void initSearchHostExecutor() {
        shutdownSearchHost();
        mSearchHostExecutor = Executors.newFixedThreadPool(5);
    }

    /**
     * 将IP最后一个.的网段1~255分成5份分别用5个线程去一个一个连接，其中有一个连接上后开始上传，关闭其他正在连接的线程
     */
    private void searchServerHost() {
        for (int i = 0; i < 5; i++) {
            mSearchHostExecutor.execute(new SearchServerTask(i * SEARCH_HOST_TIME, 1));
        }
    }

    /**
     * 上传至远程服务器
     */
    public void uploadToRemoteServer() {
        shutdownSearchHost();
        initUploadFileExecutor();
        startUploadFile(null);
    }

    /**
     * 初始化上传文件的线程池
     */
    private synchronized void initUploadFileExecutor() {
        shutdownUploadExcutor();
        mUploadFileExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * 开启上传文件任务
     * @param socket 传null表示上传至远程服务器
     */
    private synchronized void startUploadFile(Socket socket) {
        mUploadFileExecutor.execute(new UploadFileTask(socket));
    }

    /**
     * 关闭上传文件线程池
     */
    private synchronized void shutdownUploadExcutor() {
        if (mUploadFileExecutor != null && !mUploadFileExecutor.isShutdown()) {
            mUploadFileExecutor.shutdownNow();
        }
    }

    /**
     * 判断搜索服务器的线程池是否已经关闭
     * @return
     */
    private synchronized boolean isSearchHostShutdown() {
        return mSearchHostExecutor.isShutdown();
    }

    /**
     * 关闭搜索服务器线程池
     */
    private synchronized void shutdownSearchHost() {
        if (mSearchHostExecutor != null && !isSearchHostShutdown()) {
            mSearchHostExecutor.shutdownNow();
        }
    }

    /**
     * 判断搜索本地服务器是否完成
     * @return
     */
    private synchronized boolean isSearHostFinished() {
        return mSearhHostFinishedCount == 255;
    }

    /**
     * 增加搜索服务器次数的计数值
     */
    private synchronized void addSearHostFinishedCount() {
        if (!isSearHostFinished()) {
            mSearhHostFinishedCount += SEARCH_HOST_TIME;
        }
    }

    private class SearchServerTask implements Runnable {

        private int mIPOffset;
        private int mIPCount;

        public SearchServerTask(int ipOffset, int ipCount) {
            this.mIPOffset = ipOffset;
            this.mIPCount = ipCount;
        }

        @Override
        public void run() {
            Socket socket = null;
            try {
                String ipAddress = mIPAddress + (mIPOffset + mIPCount);
                log("connecting "+ ipAddress);
                socket = new Socket();
                SocketAddress address = new InetSocketAddress(ipAddress, SERVER_PORT);
                socket.connect(address, 500);
                shutdownSearchHost();
                log("stop searching server host");
                initUploadFileExecutor();
                startUploadFile(socket);
            } catch (Exception e) {
                closeSocket(socket);
                mIPCount++;
                if (mIPCount > SEARCH_HOST_TIME) {
                    log("didn't find available server from " + mIPAddress + (mIPOffset + 1) + " to " +
                            mIPAddress + (mIPOffset + SEARCH_HOST_TIME));

                    addSearHostFinishedCount();
                    if (isSearHostFinished()) {
                        log("finished searching host, didn't find available server");
                        callRefreshView(RefreshViewType.UPLOAD_FAILED, "无可用服务器");
                    }
                    return;
                }

                if (isSearchHostShutdown()) {
                    return;
                }

                mSearchHostExecutor.execute(new SearchServerTask(mIPOffset, mIPCount));
            }
        }

        private void closeSocket(Socket socket) {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }

    private class UploadFileTask implements Runnable {

        private Socket socket;

        /**
         *
         * @param socket 传null表示连接远程服务器
         */
        public UploadFileTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            InputStream is = null;
            OutputStream os = null;
            FileInputStream fis = null;
            try {
                if (socket == null) {
                    //socket值为null，连接远程服务器
                    socket = new Socket();
                    SocketAddress address = new InetSocketAddress(HOST, SERVER_PORT);
                    socket.connect(address, 15000);
                }

                File file = new File(getFilePath());
                is = socket.getInputStream();
                os = socket.getOutputStream();
                fis = new FileInputStream(file);

                log("socket connected");
                writeLine(os, START_UPLOAD);

                log("waiting server echo");
                String info = readLine(is);
                if (OK.equals(info)) {
                    log("send upload file length: " + file.length() + " bytes");
                    mTotalLength = file.length();
                    writeLine(os, String.valueOf(mTotalLength));

                    info = readLine(is);
                    if (OK.equals(info)) {
                        log("start upload");
                        callRefreshView(RefreshViewType.REFRESH_UPLOAD_INFO, "正在上传0%");

                        long startTime = System.currentTimeMillis();
                        startReadProgressTimer();
                        uploadFileData(fis, os);
                        stopReadProgressTimer();
                        log("upload progress 100%");
                        callRefreshView(RefreshViewType.REFRESH_UPLOAD_INFO, "正在上传100%");
                        log("upload file time use: " + (System.currentTimeMillis() - startTime) + "ms");
                        while (true) {
                            info = readLine(is);
                            if (FINISHED.equals(info)) {
                                log("upload success");
                                callRefreshView(RefreshViewType.UPLOAD_SUCCESS, "上传成功");
                                break;
                            }
                        }
                    } else {
                        callRefreshView(RefreshViewType.UPLOAD_FAILED, "上传失败，服务器无回应");
                    }
                } else {
                    callRefreshView(RefreshViewType.UPLOAD_FAILED, "上传失败，服务器无回应");
                }

            } catch (Exception e) {
                log(e.getMessage());
                callRefreshView(RefreshViewType.UPLOAD_FAILED, "上传失败, 错误信息：" + e.getMessage());
            } finally {
                closeInputStream(fis, "FileOutputStream");
                closeSocket(socket);
            }
        }

        /**
         * 读一行字符串
         * @param is
         * @return
         * @throws IOException
         */
        private String readLine(InputStream is) throws IOException {
            int len;
            byte[] bytes = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
                if (sb.indexOf(TERMINATOR) != -1) {
                    sb.delete((sb.length() - TERMINATOR.length()), sb.length());
                    break;
                }
            }
            return sb.toString();
        }

        /**
         * 写一行字符串
         * @param os
         * @param msg
         * @throws IOException
         */
        private void writeLine(OutputStream os, String msg) throws IOException {
            os.write(msg.getBytes());
            os.write(TERMINATOR.getBytes());
            os.flush();
        }

        /**
         * 用IO流上传文件
         * @param fis 上传文件的输入流
         * @param os 上传文件的输出流
         * @return
         */
        private void uploadFileData(FileInputStream fis, OutputStream os) throws IOException {
            int len;
            byte[] bytes = new byte[4096];
            mUploadingLength = 0;
            while ((len = fis.read(bytes)) != -1) {
                os.write(bytes, 0, len);
                os.flush();
                mUploadingLength += len;
            }
        }

        private void closeInputStream(InputStream is, String name) {
            try {
                is.close();
                log(name + " close");
            } catch (Exception e) {
                log(name + " close failed, " + e.getMessage());
            }
        }

        private void closeOutputStream(OutputStream os, String name) {
            try {
                os.close();
                log(name + " close");
            } catch (Exception e) {
                log(name + " close failed, " + e.getMessage());
            }
        }

        private void closeSocket(Socket socket) {
            try {
                socket.close();
                log("Socket close");
            } catch (Exception e) {
                log("Socket close failed, " + e.getMessage());
            }
        }
    }

    /**
     * 开启定时更新上传进度的定时器
     */
    private void startReadProgressTimer() {
        mFuture = mTimerExecutor.scheduleAtFixedRate(new ReadProgressTimerTask(), 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 关闭定时更新上传进度的定时器
     */
    private void stopReadProgressTimer() {
        if (mFuture != null) {
            mFuture.cancel(true);
        }
    }

    private class ReadProgressTimerTask implements Runnable {

        @Override
        public void run() {
            int progress = (int)(mUploadingLength / (mTotalLength * 1.0f) * 100);
            log("upload progress" + progress + "%");
            callRefreshView(RefreshViewType.REFRESH_UPLOAD_INFO, "正在上传" + progress + "%");
        }
    }

    /**
     * 获取文件目录
     * @return
     */
    private String getFilePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + ".YouAccounts" +
                File.separator + "db" +
                File.separator + "YouAccounts.db";
    }

    private void log(String msg) {
        LG.i(UploadModel.TAG + msg);
    }
}
