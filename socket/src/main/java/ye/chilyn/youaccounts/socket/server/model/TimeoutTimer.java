package ye.chilyn.youaccounts.socket.server.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alex on 2018/5/7.
 */

public class TimeoutTimer {

    private ScheduledExecutorService mCountService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;
    private TimeoutCallback mCallback;

    public TimeoutTimer(TimeoutCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 开始倒计时
     */
    public void startTimeoutCount(int id) {
        cancelTask();
        mFuture = mCountService.scheduleAtFixedRate(new TimeoutTask(id),
                15, 15, TimeUnit.SECONDS);
    }

    /**
     * 取消倒计时
     */
    public void cancelTask() {
        if (mFuture != null) {
            mFuture.cancel(true);
        }
    }

    /**
     * 关闭定时器线程池
     */
    public void shutdown() {
        if (mCountService != null) {
            mCountService.shutdownNow();
        }
    }

    private class TimeoutTask implements Runnable {

        private int id;

        public TimeoutTask(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            if (mCallback != null) {
                mCallback.onTimeout(id);
            }

            cancelTask();
        }
    }

    public interface TimeoutCallback {
        void onTimeout(int id);
    }
}
