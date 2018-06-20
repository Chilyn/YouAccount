package ye.chilyn.youaccounts.socket.server.task;

import ye.chilyn.youaccounts.socket.constants.Constants;
import ye.chilyn.youaccounts.socket.server.util.Printer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class AcceptSocketLoopTask implements Runnable {

    private AcceptedSocketCallback mCallback;
    private List<String> mIpBlackList;

    public AcceptSocketLoopTask(AcceptedSocketCallback callback,
                                List<String> ipBlackList) {
        this.mCallback = callback;
        mIpBlackList = ipBlackList;
    }

    @Override
    public void run() {
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

                Printer.print("Accepted a socket, Socket ID: " + count);
                if (mCallback != null) {
                    mCallback.onAcceptedSocket(socket, count);
                }
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

    private void closeSocket(Socket socket, int id) {
        try {
            socket.close();
            Printer.print("socket close, Socket ID: " + id);
        } catch (Exception e) {
            Printer.print("socket close failed, Socket ID: " + id);
        }
    }

    private boolean isSocketIpInBlackList(Socket socket, int id) {
        synchronized (mCallback) {
            InetAddress address = socket.getInetAddress();
            if (address == null) {
                return false;
            }

            String ipAddress = address.getHostAddress();
            if (!mIpBlackList.contains(ipAddress)) {
                return false;
            }

            Printer.print("Accepted a attack socket, ip address " + ipAddress
                    + ", Socket ID: " + id);
            return true;
        }
    }

    /**
     * 接收到Socket的回调
     */
    public interface AcceptedSocketCallback {
        void onAcceptedSocket(Socket socket, int id);
    }
}
