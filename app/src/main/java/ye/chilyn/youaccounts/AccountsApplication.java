package ye.chilyn.youaccounts;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alex on 2018/1/16.
 */

public class AccountsApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
