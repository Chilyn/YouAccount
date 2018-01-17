package ye.chilyn.youaccounts.contant;

import android.os.Environment;

import java.io.File;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;

/**
 * Created by Alex on 2018/1/17.
 */

public final class AppFilePath {
    private AppFilePath(){}
    public static final String EXTERNAL_PATH = Environment.getExternalStorageDirectory() + File.separator +
            AccountsApplication.getAppContext().getString(R.string.external_dir) + File.separator;

    public static final String DB_FILE_PATH = EXTERNAL_PATH +
            AccountsApplication.getAppContext().getString(R.string.db_dir) + File.separator;

    public static void createAppDirectories() {
        File file = new File(DB_FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
