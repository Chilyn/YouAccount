package ye.chilyn.youaccount.socket.server.util;

import java.io.File;
import java.io.IOException;

public final class FileUtil {
    private FileUtil() {
    }

    public static boolean createFile(File file) throws IOException {
        if (file.exists()) {
            return true;
        }

        return file.createNewFile();
    }

    public static boolean createFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }

        return file.createNewFile();
    }

    public static boolean createDir(File file) {
        if (file.exists()) {
            return true;
        }

        return file.mkdirs();
    }

    public static boolean createDir(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }

        return file.mkdirs();
    }

    public static boolean isDirExists(String path) {
        return isFileExists(path);
    }

    public static boolean isFileExists(String path) {
        return new File(path).exists();
    }
}
