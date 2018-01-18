package ye.chilyn.youaccounts.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alex on 2018/1/17.
 */

public class DateUtil {

    private static final Calendar calendar1 = Calendar.getInstance();
    private static final Calendar calendar2 = Calendar.getInstance();

    public static boolean isTheSameDay(Date date1, Date date2) {
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isTheSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static long getThisWeekStartTime(Date now) {
        calendar1.setTime(now);
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);
        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        return calendar1.getTimeInMillis();
    }

    public static long getThisWeekEndTime(Date now) {
        calendar1.setTime(now);
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);
        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MILLISECOND, 999);
        return calendar1.getTimeInMillis();
    }
}
