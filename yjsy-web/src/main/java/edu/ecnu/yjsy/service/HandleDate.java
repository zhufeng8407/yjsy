package edu.ecnu.yjsy.service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 增加处理日期的方法。
 *
 * @author xulinhao
 */

public abstract class HandleDate {

    public static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd");

    private static final DateFormat FMT_NYR = new SimpleDateFormat("yyyyMMdd");

    private static final DateFormat FMT_XSF = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static final DateFormat FMT_UTC = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS Z");

    /**
     * 给定日期格式为:<code>yyyyMMdd</code>或<code>yyyy-MM-dd</code>
     *
     * @param date
     *            给定日期格式为:<code>yyyyMMdd</code>
     * @return
     * @throws ParseException
     */
    protected Date getDateByNYR(String date) {
        if (date == null) return null;
        if (date.contains("-")) {
            date = date.substring(0, 4) + date.substring(5, 7)
                    + date.substring(8, 10);
        }
        try {
            return new Date(FMT_NYR.parse(date).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 给定日期格式为:<code>yyyy-MM-dd HH:mm:ss</code>
     *
     * @param date
     *            给定日期格式为:<code>yyyy-MM-dd HH:mm:ss</code>
     * @return
     */
    protected Date getDateByXSF(String date) {
        if (date == null) return null;
        try {
            return new Date(FMT_XSF.parse(date).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 给定日期格式为:<code>yyyy-MM-dd'T'HH:mm:ss.SSS Z</code>
     *
     * @param date
     *            给定日期格式为:<code>yyyy-MM-dd'T'HH:mm:ss.SSS Z</code>
     * @return
     * @throws ParseException
     */
    protected Date getDateByUTC(String date) throws ParseException {
        date = date.replace("Z", " UTC");
        return new Date(FMT_UTC.parse(date).getTime());
    }

    /**
     * 返回学年。
     *
     * @return
     */
    protected String getSchoolYear() {
        Calendar day = Calendar.getInstance();
        int year = day.get(Calendar.YEAR);
        int month = day.get(Calendar.MONTH);

        String result = year + "-" + (year + 1);
        if (month >= 8 || month < 2) {
            result = year + "-" + (year + 1);
        } else if ((month > 2) && month <= 7) {
            result = (year - 1) + "-" + year;
        }

        return result;
    }

    /**
     * 返回学期。
     *
     * @return
     */
    protected short getSchoolTerm() {
        short result = 1;

        Calendar day = Calendar.getInstance();
        int month = day.get(Calendar.MONTH);
        if (month >= 8 || month < 2) {
            result = 1;
        } else if ((month > 2) && month <= 7) {
            result = 2;
        }

        return result;
    }

    /**
     * 计算两个日期之间的天数。
     *
     * @param start
     *            开始日期
     * @param end
     *            结束日期
     * @return
     */
    protected int getDaysBetween(Date start, Date end) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(start);
        long time1 = cal.getTimeInMillis();

        cal.setTime(end);
        long time2 = cal.getTimeInMillis();

        if (time1 > time2) { throw new IllegalArgumentException("开始日期大于结束日期"); }

        long between_days = (time2 - time1) / (3600000 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    // ----------------------
    // 用于学籍异动的日期处理
    // ----------------------

    protected java.util.Date getDateAddHalfYear(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.getDate(date));
        cal.add(Calendar.MONTH, 6);
        return cal.getTime();
    }

    protected java.util.Date getDateAddYear(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.getDate(date));
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    protected java.util.Date getDateSubtractHalfYear(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.getDate(date));
        cal.add(Calendar.MONTH, -6);
        return cal.getTime();
    }

    protected java.util.Date getDateSubtractYear(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.getDate(date));
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    protected java.util.Date getDateSubtractOneHalfYear(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.getDate(date));
        cal.add(Calendar.MONTH, -18);
        return cal.getTime();
    }

    protected java.util.Date getDateSubtractTwoYears(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.getDate(date));
        cal.add(Calendar.YEAR, -2);
        return cal.getTime();
    }

    protected java.util.Date getNextDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 如果给定的日期变量为空，则返回当前日期。
     *
     * @param date
     * @return
     */
    private java.util.Date getDate(java.util.Date date) {
        if (date == null) { return new java.util.Date(); }
        return date;
    }

}
