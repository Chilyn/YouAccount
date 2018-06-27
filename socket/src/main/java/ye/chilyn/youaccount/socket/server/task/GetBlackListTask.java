package ye.chilyn.youaccount.socket.server.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccount.socket.server.util.FilePath;
import ye.chilyn.youaccount.socket.server.util.FileUtil;
import ye.chilyn.youaccount.socket.server.util.Printer;

/**
 * Created by Alex on 2018/6/27
 */
public class GetBlackListTask implements Runnable {

    private List<String> mIpBlackList = new ArrayList<>();
    private GetBlackListCallback mCallback;

    public GetBlackListTask(GetBlackListCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void run() {
        if (!FileUtil.isFileExists(FilePath.BLACK_LIST_FILE_PATH)) {
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FilePath.BLACK_LIST_FILE_PATH));
            String ipAddress;
            while ((ipAddress = reader.readLine()) != null) {
                mIpBlackList.add(ipAddress);
            }

            callback();
            reader.close();
        } catch (Exception e) {
            Printer.print("get black list from file failed, " + e.getMessage());
        }
    }

    /**
     * 回调返回读取到的IP
     */
    private void callback(){
        if (mCallback != null) {
            mCallback.onGetBlackListSuccess(mIpBlackList);
        }
    }

    public interface GetBlackListCallback {
        void onGetBlackListSuccess(List<String> ipBlackList);
    }
}
