package ye.chilyn.youaccounts;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alex on 2018/1/16.
 */

public class AccountsApplication extends Application {

    private static Context mContext;
    /**是否可以写文件标识*/
    private static boolean mCanCreateFile = false;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }

    /**
     * 是否可以做写文件操作
     * @return
     */
    public static boolean canCreateFile() {
        return mCanCreateFile;
    }

    public static void setCanCreateFile(boolean canCreateFile) {
        mCanCreateFile = canCreateFile;
    }
}
