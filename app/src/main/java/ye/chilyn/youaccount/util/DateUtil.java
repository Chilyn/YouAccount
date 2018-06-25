package ye.chilyn.youaccount.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alex on 2018/1/17.
 * 日期操作工具类
 */

public class DateUtil {

    private static final Calendar calendar1 = Calendar.getInstance();
    private static final Calendar calendar2 = Calendar.getInstance();

    /**
     * 是否是同一日期
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isTheSameDate(Date date1, Date date2) {
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 是否是同一日期
     * @param calendar1
     * @param calendar2
     * @return
     */
    public static boolean isTheSameDate(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 是否是今天
     * @param timeMill
     * @return
     */
    public static boolean isToday(long timeMill) {
        calendar1.setTimeInMillis(timeMill);
        calendar2.setTime(new Date());
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 是否是昨天
     * @param timeMill
     * @return
     */
    public static boolean isYesterday(long timeMill) {
        calendar1.setTimeInMillis(timeMill);
        calendar2.setTime(new Date());
        calendar2.add(Calendar.DATE, -1);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 是否是这个月
     * @param date
     * @return
     */
    public static boolean isThisMonth(Date date) {
        calendar1.setTime(new Date());
        calendar2.setTime(date);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
    }

    /**
     * 是否时间相差超过6个月
     * @param startDate
     * @param endState
     * @return
     */
    public static boolean isOverSixMonth(Date startDate, Date endState) {
        calendar1.setTime(startDate);
        calendar1.add(Calendar.MONTH, 6);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        calendar2.setTime(endState);
        return calendar2.getTimeInMillis() >= calendar1.getTimeInMillis();
    }

    /**
     * 获取本周开始时间
     * @param now
     * @return
     */
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

    /**
     * 获取本周结束时间
     * @param now
     * @return
     */
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

    /**
     * 获取传入参数所在月份的开始时间
     * @param date
     * @return
     */
    public static long getMonthStartTime(Date date) {
        calendar1.setTime(date);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        return calendar1.getTimeInMillis();
    }

    /**
     * 获取传入参数所在月份的结束时间
     * @param date
     * @return
     */
    public static long getMonthEndTime(Date date) {
        calendar1.setTime(date);
        calendar1.set(Calendar.DAY_OF_MONTH, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MILLISECOND, 999);
        return calendar1.getTimeInMillis();
    }

    /**
     * 获取传入参数所在日期的开始时间
     * @param date
     * @return
     */
    public static long getDateStartTime(Date date) {
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        return calendar1.getTimeInMillis();
    }

    /**
     * 获取传入参数所在日期的结束时间
     * @param date
     * @return
     */
    public static long getDateEndTime(Date date) {
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MILLISECOND, 999);
        return calendar1.getTimeInMillis();
    }
}
