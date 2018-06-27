package ye.chilyn.youaccount.socket.server;

import ye.chilyn.youaccount.socket.server.task.AcceptFileTask;
import ye.chilyn.youaccount.socket.constants.Constants;
import ye.chilyn.youaccount.socket.server.task.AddBlackListToFileTask;
import ye.chilyn.youaccount.socket.server.task.GetBlackListTask;
import ye.chilyn.youaccount.socket.server.util.Printer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
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

public class SocketServer implements AcceptFileTask.AddBlackListListener, GetBlackListTask.GetBlackListCallback {

    private ExecutorService mSocketTaskExecutor = Executors.newSingleThreadExecutor();
    private ExecutorService mBlackListTaskExecutor = Executors.newSingleThreadExecutor();
    private List<String> mIpBlackList = new ArrayList<>();
    private List<Map<String, Object>> mTimeoutIpList = new ArrayList<>();

    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        server.getBlackList();
        server.startServer();
    }

    private void getBlackList() {
        mBlackListTaskExecutor.execute(new GetBlackListTask(this));
    }

    private void startServer() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(Constants.SERVER_PORT);
            Printer.print("Server started");
            int count = 0;
            while (true) {
                Socket socket = server.accept();
                if (count == Integer.MAX_VALUE) {
                    count = 0;
                } else {
                    count++;
                }

                if (isSocketIpInBlackList(socket, count)) {
                    closeSocket(socket, count);
                    continue;
                }

                String ipAddress = getSocketIpAddress(socket);
                Printer.printWithSpaceLine("Accepted a socket, ip address " + ipAddress + ", Socket ID: " + count);
                mSocketTaskExecutor.execute(new AcceptFileTask(socket, ipAddress, count, this));
            }
        } catch (Exception e) {
            Printer.print("Server Error:" + e.getMessage());
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.exit(0);
        }
    }

    private String getSocketIpAddress(Socket socket) {
        InetAddress address = socket.getInetAddress();
        if (address == null) {
            return null;
        }

        return address.getHostAddress();
    }

    private void closeSocket(Socket socket, int id) {
        try {
            socket.close();
            Printer.print("socket close, Socket ID: " + id);
        } catch (Exception e) {
            Printer.print("socket close failed, Socket ID: " + id);
        }
    }

    private boolean isSocketIpInBlackList(Socket socket, int id) {
        synchronized (this) {
            String ipAddress = getSocketIpAddress(socket);
            if (ipAddress == null || "".equals(ipAddress)) {
                return false;
            }

            if (!mIpBlackList.contains(ipAddress)) {
                return false;
            }

            Printer.printWithSpaceLine("Accepted an attack socket, ip address " + ipAddress
                    + ", Socket ID: " + id);
            return true;
        }
    }

    @Override
    public void onGetBlackListSuccess(List<String> ipBlackList) {
        if (ipBlackList.size() == 0) {
            return;
        }

        synchronized (this) {
            mIpBlackList.addAll(ipBlackList);
        }
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

        mBlackListTaskExecutor.execute(new AddBlackListToFileTask(ipAddress, socketId));
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
