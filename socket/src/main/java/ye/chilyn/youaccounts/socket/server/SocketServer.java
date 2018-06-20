package ye.chilyn.youaccounts.socket.server;

import ye.chilyn.youaccounts.socket.constants.Constants;
import ye.chilyn.youaccounts.socket.server.task.AcceptFileTask;
import ye.chilyn.youaccounts.socket.server.task.AcceptSocketLoopTask;
import ye.chilyn.youaccounts.socket.server.util.Printer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Alex on 2018/6/6.
 */

public class SocketServer implements AcceptSocketLoopTask.AcceptedSocketCallback, AcceptFileTask.AddBlackListListener {

    private ExecutorService mServerTaskExecutor = Executors.newFixedThreadPool(3);
    private List<String> mIpBlackList = new ArrayList<>();
    private List<Map<String, Object>> mTimeoutIpList = new ArrayList<>();

    public static void main(String[] args) {
        new SocketServer().startServer();
    }
    
    public void startServer() {
        mServerTaskExecutor.execute(new AcceptSocketLoopTask(this,
                mIpBlackList));
    }

    @Override
    public void onAcceptedSocket(Socket socket, int id) {
        mServerTaskExecutor.execute(new AcceptFileTask(socket, id, this));
    }

    @Override
    public void onAddBlackList(String ipAddress, int socketId) {
        if (ipAddress == null || "".equals(ipAddress)) {
            Printer.print("add attacker's ip failed, ip is empty, Socket ID: " + socketId);
            return;
        }

        synchronized (this) {
            if (mIpBlackList.contains(ipAddress)) {
                Printer.print("add attacker's ip failed, ip has added before, Socket ID: " + socketId);
                return;
            }

            Printer.print("new attacker's ip " + ipAddress + ", Socket ID: " + socketId);
            mIpBlackList.add(ipAddress);
        }
    }

    @Override
    public void onAddTimeoutIp(String ipAddress, int socketId) {
        if (ipAddress == null || "".equals(ipAddress)) {
            Printer.print("add timeout ip failed, ip is empty, Socket ID: " + socketId);
            return;
        }

        synchronized (this) {
            for (Map<String, Object> timeoutIp : mTimeoutIpList) {
                String timeoutIpAddress = (String) timeoutIp.get(Constants.TIMEOUT_IP);
                if (!timeoutIpAddress.equals(ipAddress)) {
                    continue;
                }

                int timeoutTime = (int) timeoutIp.get(Constants.TIMEOUT_IP_TIME);
                timeoutTime++;
                timeoutIp.put(Constants.TIMEOUT_IP_TIME, timeoutTime);
                Printer.print("plus timeout ip time, ip " + ipAddress + ", time " + timeoutTime + ", Socket ID: " + socketId);
                if (timeoutTime == 3) {
                    onAddBlackList(timeoutIpAddress, socketId);
                }
                return;
            }

            Printer.print("new timeout ip " + ipAddress + ", Socket ID: " + socketId);
            Map<String, Object> newTimeoutIp = new HashMap<>();
            newTimeoutIp.put(Constants.TIMEOUT_IP_TIME, 1);
            newTimeoutIp.put(Constants.TIMEOUT_IP, ipAddress);
            mTimeoutIpList.add(newTimeoutIp);
        }
    }
}
