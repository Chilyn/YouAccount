package ye.chilyn.youaccount.socket.server.task;

import ye.chilyn.youaccount.socket.constants.Constants;
import ye.chilyn.youaccount.socket.server.model.TimeoutTimer;
import ye.chilyn.youaccount.socket.server.util.FilePath;
import ye.chilyn.youaccount.socket.server.util.Printer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class AcceptFileTask implements Runnable, TimeoutTimer.TimeoutCallback {

    private Socket socket;
    private AddBlackListListener mBlackListListener;
    private int id;
    private String mUploadFilePath, mLogFilePath;
    private Printer mPrinter;
    private TimeoutTimer mTimer = new TimeoutTimer(this);
    private int mReadLineId = 0;

    public AcceptFileTask(Socket socket, int id, AddBlackListListener listener) {
        this.socket = socket;
        this.id = id;
        mBlackListListener = listener;
        initFilePath();
        initLog();
    }

    /**
     * 初始化并创建文件
     */
    private void initFilePath() {
        long time = System.currentTimeMillis();
        mUploadFilePath = FilePath.createUploadFilePath(time);
        mLogFilePath = FilePath.createLogFilePath(time);
    }

    /**
     * 初始化日志文件相关操作
     */
    private void initLog() {
        mPrinter = new Printer(mLogFilePath, id);
    }

    @Override
    public void run() {
        InputStream is;
        OutputStream os;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            log("waiting client's command");
            increaseReadLineId();
            mTimer.startTimeoutCount(getReadLineId());
            String info = readLine(is);
            mTimer.cancelTask();
            if (Constants.START_UPLOAD.equals(info)) {
                writeLine(os, Constants.OK);

                //获取文件长度
                increaseReadLineId();
                mTimer.startTimeoutCount(getReadLineId());
                String fileLength = readLine(is);
                mTimer.cancelTask();
                long totalLength = Long.valueOf(fileLength);
                writeLine(os, Constants.OK);

                //开始接收文件
                log("start accepting upload file, file length: " + totalLength + " bytes");
                writeFileData(totalLength, is);
                writeLine(os, Constants.FINISHED);
            } else {
                log("unknown command " + info);
                addBlackIpAddress(socket);
            }
        } catch (Exception e) {
            log("error info:" + e.getMessage());
        } finally {
            mTimer.shutdown();
            closeSocket(socket);
            mPrinter.closeLogOutputStream();
        }
    }

    private synchronized int getReadLineId() {
        return mReadLineId;
    }

    private synchronized void increaseReadLineId() {
        mReadLineId++;
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
            if (sb.indexOf(Constants.TERMINATOR) != -1) {
                sb.delete((sb.length() - Constants.TERMINATOR.length()), sb.length());
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
        os.write(Constants.TERMINATOR.getBytes());
        os.flush();
    }

    /**
     * 用IO流写文件
     * @param is
     * @return
     */
    private void writeFileData(long totalLength, InputStream is) throws Exception {
        FilePath.checkDirCreation();
        FileOutputStream fos = new FileOutputStream(mUploadFilePath);
        log("accepting file path: " + mUploadFilePath);
        long currentLength = 0;
        int len;
        byte[] bytes = new byte[4096];
        while ((len = is.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
            fos.flush();
            currentLength += len;
            if (totalLength == currentLength) {
                break;
            }
        }

        log("finish accepting file ");
        log("accepted file length: " + currentLength + " bytes");
        closeOutputStream(fos, "FileOutputStream");
        return;
    }

    private void closeOutputStream(OutputStream os, String name) {
        if (os != null) {
            try {
                os.close();
                log(name + " close");
            } catch (IOException e) {
                log(name + " close failed, " + e.getMessage());
            }
        }
    }

    private void closeSocket(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
                log("Socket close");
            } catch (IOException e) {
                log("Socket close failed, " + e.getMessage());
            }
        }
    }

    /**
     * 用IO流输出上传文件log
     * @param msg
     */
    private void log(String msg) {
        mPrinter.log(msg + ", Socket ID:" + id);
    }

    @Override
    public void onTimeout(int id) {
        if (id == getReadLineId()) {
            log("read command timeout");
            addTimeoutIpAddress(socket);
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 将攻击者ip地址添加到黑名单
     * @param socket
     */
    private void addBlackIpAddress(Socket socket) {
        String ipAddress = getIpAddress(socket, "attacker's");
        log("attacker's ip address: " + ipAddress);
        if (mBlackListListener != null) {
            mBlackListListener.onAddBlackList(ipAddress, id);
        }
    }

    /**
     * 将读命令超时的ip地址登记
     * @param socket
     */
    private void addTimeoutIpAddress(Socket socket) {
        String ipAddress = getIpAddress(socket, "timeout");
        log("timeout ip address: " + ipAddress);
        if (mBlackListListener != null) {
            mBlackListListener.onAddTimeoutIp(ipAddress, id);
        }
    }

    /**
     * 获取socket的ip地址
     * @param socket
     * @param description
     * @return
     */
    private String getIpAddress(Socket socket, String description) {
        if (socket == null) {
            log("can not get " + description + " ip address, socket is null");
            return "";
        }

        InetAddress address = socket.getInetAddress();
        if (address == null) {
            log("can not get " + description + " ip address, InetAddress is null");
            return "";
        }

        return address.getHostAddress();
    }

    public interface AddBlackListListener {
        void onAddBlackList(String ipAddress, int socketId);
        void onAddTimeoutIp(String ipAddress, int socketId);
    }
}
