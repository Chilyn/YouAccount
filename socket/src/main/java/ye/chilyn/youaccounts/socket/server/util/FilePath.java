package ye.chilyn.youaccounts.socket.server.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class FilePath {
    private FilePath() {
    }

    private static final SimpleDateFormat FORMAT;
    private static final Date DATE = new Date();

    /**
     * 项目根目录路径
     */
    public static final String ROOT_DIR_PATH;

    /**
     * 服务器文件的目录路径
     */
    public static final String SERVER_DIR_PATH;

    /**
     * 控制台输出文件的目录路径
     */
    public static final String PRINT_FILE_PATH;

    /**
     * 接收文件的目录路径
     */
    public static final String ACCEPTED_DIR_PATH;

    static {
        FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        FORMAT.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        String userDir = System.getProperty("user.dir");
        ROOT_DIR_PATH = userDir + File.separator + ".YouAccounts";
        FileUtil.createDir(ROOT_DIR_PATH);

        SERVER_DIR_PATH = ROOT_DIR_PATH + File.separator + "server";
        FileUtil.createDir(SERVER_DIR_PATH);
        PRINT_FILE_PATH = SERVER_DIR_PATH + File.separator +
                "server" + getTimeString(System.currentTimeMillis()) + ".log";

        String acceptDirPath = ROOT_DIR_PATH + File.separator + "file";
        FileUtil.createDir(acceptDirPath);
        ACCEPTED_DIR_PATH  = acceptDirPath;
    }

    /**
     * 创建上传文件的路径
     * @param timeMillis 创建文件的时间
     * @return 路径
     */
    public static String createUploadFilePath(long timeMillis){
        return ACCEPTED_DIR_PATH + File.separator +
                "YouAccounts" + getTimeString(timeMillis) + ".db";
    }

    /**
     * 创建日志文件的路径
     * @param timeMillis 创建文件的时间
     * @return 路径
     */
    public static String createLogFilePath(long timeMillis){
        return ACCEPTED_DIR_PATH + File.separator +
                "YouAccounts" + getTimeString(timeMillis) + ".log";
    }

    public static void checkDirCreation() {
        FileUtil.createDir(SERVER_DIR_PATH);
        FileUtil.createDir(ACCEPTED_DIR_PATH);
    }

    /**
     * 根据传入时间格式化时间
     * @param timeMillis
     * @return
     */
    private static String getTimeString(long timeMillis) {
        DATE.setTime(timeMillis);
        return FORMAT.format(DATE);
    }
}
