package ye.chilyn.youaccounts.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Alex on 2017/5/23.
 * 缓存线程池帮助类，目的是在整个app中只有一个缓存线程池
 */

public class CacheExecutorHelper {

    private ExecutorService mCacheExecutor = Executors.newCachedThreadPool();

    private static class InstanceHolder {
        private static final CacheExecutorHelper mInstance = new CacheExecutorHelper();
    }

    private CacheExecutorHelper() {
    }

    public static CacheExecutorHelper getInstance() {
        return InstanceHolder.mInstance;
    }

    public ExecutorService getCacheExecutor() {
        return mCacheExecutor;
    }
}
