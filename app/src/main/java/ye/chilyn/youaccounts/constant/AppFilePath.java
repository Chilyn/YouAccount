package ye.chilyn.youaccounts.constant;

import android.os.Environment;

import java.io.File;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;

/**
 * Created by Alex on 2018/1/17.
 * 文件相关路径
 */

public final class AppFilePath {
    private AppFilePath(){}
    /**app外部存储目录路径*/
    public static final String EXTERNAL_PATH = Environment.getExternalStorageDirectory() + File.separator +
            AccountsApplication.getAppContext().getString(R.string.dot) +
            AccountsApplication.getAppContext().getString(R.string.app_name) + File.separator;

    /**数据库文件存储目录路径*/
    public static final String DB_FILE_PATH = EXTERNAL_PATH +
            AccountsApplication.getAppContext().getString(R.string.db) + File.separator;

    /**
     * 初始化应用目录
     */
    public static void createAppDirectories() {
        File file = new File(DB_FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
