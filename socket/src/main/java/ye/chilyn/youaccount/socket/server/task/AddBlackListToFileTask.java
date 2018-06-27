package ye.chilyn.youaccount.socket.server.task;

import java.io.FileOutputStream;

import ye.chilyn.youaccount.socket.server.util.FilePath;
import ye.chilyn.youaccount.socket.server.util.Printer;

/**
 * Created by Alex on 2018/6/27
 */
public class AddBlackListToFileTask implements Runnable {

    private String ipAddress;
    private int id;

    public AddBlackListToFileTask(String ipAddress, int id) {
        this.ipAddress = ipAddress;
        this.id = id;
    }

    @Override
    public void run() {
        if (ipAddress == null || "".equals(ipAddress)) {
            Printer.print("attacker ip write to file failed, ip address is empty, Socket ID:" + id);
            return;
        }

        FilePath.checkDirCreation();
        try {
            FileOutputStream fos = new FileOutputStream(FilePath.BLACK_LIST_FILE_PATH, true);
            fos.write(ipAddress.getBytes());
            fos.write("\r\n".getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Printer.print("attacker ip " + ipAddress + " write to file failed, " + e.getMessage() + ", Socket ID:" + id);
        }
    }
}
