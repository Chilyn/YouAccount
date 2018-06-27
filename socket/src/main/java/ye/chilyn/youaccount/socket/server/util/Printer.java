package ye.chilyn.youaccount.socket.server.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Printer {

    private static final String TAG = "SocketServer: ";
    private static final SimpleDateFormat FORMAT;
    private static final Date DATE = new Date();
    private FileOutputStream mLogOutput;
    private String mLogFilePath;
    private boolean mCanWriteLogFile = true;
    private String mLogEndString;

    static {
        setPrintFilePath();
        FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.getDefault());
        FORMAT.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    private static void setPrintFilePath() {
        try {
            PrintStream stream = new PrintStream(FilePath.PRINT_FILE_PATH);
            System.setOut(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Printer(String logFilePath, int socketId) {
        mLogFilePath = logFilePath;
        mLogEndString = ", Socket ID: " + socketId;
    }

    public static void print(String msg) {
        printWithPrefix("", msg);
    }

    public static void printWithSpaceLine(String msg) {
        printWithPrefix("\n", msg);
    }

    private static void printWithPrefix(String prefix, String msg) {
        if (!FileUtil.isFileExists(FilePath.PRINT_FILE_PATH)) {
            FilePath.checkDirCreation();
            setPrintFilePath();
        }

        DATE.setTime(System.currentTimeMillis());
        System.out.println(prefix + FORMAT.format(DATE) + TAG + msg);
    }

    public void log(String msg) {
        //log文件输出流出问题，信息由控制台输出
        if (!mCanWriteLogFile) {
            print(msg);
            return;
        }

        //创建log文件输出流
        if (mLogOutput == null) {
            initLogOutputStream();
            if (!mCanWriteLogFile) {
                print(msg);
                return;
            }

            print("Log file path: " + mLogFilePath + mLogEndString);
        }

        DATE.setTime(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        sb.append(FORMAT.format(DATE))
                .append(TAG)
                .append(msg)
                .append("\n");
        try {
            FilePath.checkDirCreation();
            mLogOutput.write(sb.toString().getBytes());
            mLogOutput.flush();
        } catch (IOException e) {
            print("write log failed, " + e.getMessage() + mLogEndString);
            print(msg);
            closeLogOutputStream();
        }
    }

    /**
     * 创建log文件输出流
     */
    private void initLogOutputStream() {
        try {
            FilePath.checkDirCreation();
            mLogOutput = new FileOutputStream(mLogFilePath);
        } catch (Exception e) {
            mCanWriteLogFile = false;
            print("create log output stream failed, " + e.getMessage() +
                    mLogEndString);
        }
    }

    /**
     * 关闭log文件输出流
     */
    public void closeLogOutputStream() {
        mCanWriteLogFile = false;
        try {
            mLogOutput.close();
            print("LogOutputStream close" + mLogEndString);
        } catch (Exception e) {
            print("LogOutputStream close failed, " + e.getMessage() +
                  mLogEndString);
        }
    }
}
