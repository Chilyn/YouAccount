package ye.chilyn.youaccount.socket.client;

import ye.chilyn.youaccount.socket.constants.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Alex on 2018/6/6.
 */

public class SocketClient {

    public static final String TAG = "SocketClient: ";
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        new SocketClient().connectServer();
    }

    public void connectServer() {
        executor.execute(new ConnectServerTask());
    }

    private void log(String msg) {
        System.out.println(TAG + msg);
    }

    private class ConnectServerTask implements Runnable {

        @Override
        public void run() {
            try {
                File file = new File("C:\\Users\\Admin\\Desktop\\b\\YouAccounts.db");
                Socket socket = new Socket("localhost", Constants.SERVER_PORT);
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                FileInputStream fileInput = new FileInputStream(file);

                log("socket started");
                os.write(Constants.START_UPLOAD.getBytes());
                os.write(Constants.TERMINATOR.getBytes());
                os.flush();

                log("waiting server echo");
                int len;
                byte[] bytes = new byte[4096];
                StringBuilder sb = new StringBuilder();
                while ((len = is.read(bytes)) != -1) {
                    sb.append(new String(bytes, 0, len));
                    if (sb.indexOf(Constants.TERMINATOR) != -1) {
                        sb.delete((sb.length() - Constants.TERMINATOR.length()), sb.length());
                        break;
                    }
                }

                String info = sb.toString();
                if (Constants.OK.equals(info)) {
                    log("send upload file length: " + file.length() + " bytes");
                    long totalLength = file.length();
                    os.write(String.valueOf(totalLength).getBytes());
                    os.write(Constants.TERMINATOR.getBytes());
                    os.flush();

                    sb = new StringBuilder();
                    while ((len = is.read(bytes)) != -1) {
                        sb.append(new String(bytes, 0, len));
                        if (sb.indexOf(Constants.TERMINATOR) != -1) {
                            sb.delete((sb.length() - Constants.TERMINATOR.length()), sb.length());
                            break;
                        }
                    }

                    info = sb.toString();
                    if (Constants.OK.equals(info)) {
                        log("start upload");
                        long currentLength = 0;
                        long lastProgress = 0;
                        long startTime = System.currentTimeMillis();
                        while ((len = fileInput.read(bytes)) != -1) {
                            os.write(bytes, 0, len);
                            os.flush();
                            currentLength += len;
                            int progress = (int)(currentLength / (totalLength * 1d) * 100);
                            if ((progress - lastProgress) >= 5) {
                                log("upload progress" + progress + "%");
                                lastProgress = progress;
                            }
                        }

                        fileInput.close();
                        log("upload file time use: " + (System.currentTimeMillis() - startTime) + "ms");
                        while (true) {
                            sb = new StringBuilder();
                            while ((len = is.read(bytes)) != -1) {
                                sb.append(new String(bytes, 0, len));
                                if (sb.indexOf(Constants.TERMINATOR) != -1) {
                                    sb.delete((sb.length() - Constants.TERMINATOR.length()), sb.length());
                                    break;
                                }
                            }

                            info = sb.toString();
                            if (Constants.FINISHED.equals(info)) {
                                log("upload success");
                                break;
                            }
                        }
                    }
                }

                is.close();
                os.close();
                fileInput.close();
                socket.close();
                log("client close");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    }
}
