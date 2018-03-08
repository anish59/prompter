package bbt.com.prompter.helper;


import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateHelper {

//    D	day in year	(Number)	189
//    E	day of week	(Text)	E/EE/EEE:Tue, EEEE:Tuesday, EEEEE:T
//    F	day of week in month	(Number)	2 (2nd Wed in July)
//    G	era designator	(Text)	AD
//    H	hour in day (0-23)	(Number)	0
//    K	hour in am/pm (0-11)	(Number)	0
//    L	stand-alone month	(Text)	L:1 LL:01 LLL:Jan LLLL:January LLLLL:J
//    M	month in year	(Text)	M:1 MM:01 MMM:Jan MMMM:January MMMMM:J
//    S	fractional seconds	(Number)	978
//    W	week in month	(Number)	2
//    Z	time zone (RFC 822)	(Time Zone)	Z/ZZ/ZZZ:-0800 ZZZZ:GMT-08:00 ZZZZZ:-08:00
//    a	am/pm marker	(Text)	PM
//    c	stand-alone day of week	(Text)	c/cc/ccc:Tue, cccc:Tuesday, ccccc:T
//    d	day in month	(Number)	10
//    h	hour in am/pm (1-12)	(Number)	12
//    k	hour in day (1-24)	(Number)	24
//    m	minute in hour	(Number)	30
//    s	second in minute	(Number)	55
//    w	week in year	(Number)	27
//    y	year	(Number)	yy:10 y/yyy/yyyy:2010
//    z	time zone	(Time Zone)	z/zz/zzz:PST zzzz:Pacific Standard Time
//    '	escape for text	(Delimiter)	'Date=':Date=
//            ''	single quote	d(Literal)	'o''clock':o'clock


    public static final String hhmmAMPM = "hh:mm a";


    public static final String dd = "dd";
    public static final String MMM = "MMM";
    public static final String yyyy = "yyyy";

    public static final String yyyyMMDD = "yyyy-MM-dd";
    public static final String DD_MMM_YYYY = "dd-MMM-yyyy";
    public static final String DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DD_MM_YYYY_HH_MM_A = "dd-MM-yyyy hh:mm a";//STORED DATE FORMAT
    public static final String YYYY_MMDD_HHMMSS = "yyyyMMddHHmmss";
    public static final String DD_MM_YYYY_HH_MM = "dd-MM-yyyy HH:mm";

    public static final String dd_MM_yyyy_HH_mm = "dd-MM-yyyy-HH-mm";//input for date selection dialog
    public static final String dd_MMM_yyyy_hh_mm_a = "dd MMM yyyy , hh:mm a";//output for date selection dialog


    public static final int StartTime = 4;
    public static final int EndTime = 3;

    public static final Calendar calendar = Calendar.getInstance();

    public static String dateToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date).trim();
    }

    public static Date stringToDate(String dateStr, String pattern)
            throws Exception {
        return new SimpleDateFormat(pattern).parse(dateStr);
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Calendar stringToCalendar(String inputDate, String inputFormat) {
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
            cal.setTime(sdf.parse(inputDate));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String formatDate(Date date, String type) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(type, Locale.US);
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDatemYYYYMMDD(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM_A, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(yyyyMMDD);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    //date should be in the yyyy-DD-MM format
    public static boolean isCurrentDateSunday(String date) {
        Date currentDate = DateHelper.parseDate(date, DateHelper.yyyyMMDD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

    public static String formatDatemDDMMMYYYY(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM_A, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(DD_MMM_YYYY);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatDatemDDMMYYYYhhmma(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM_A);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatTransactionDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM_A);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static long formatDatemDDMMYYYYhhmmaint(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(YYYY_MMDD_HHMMSS);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.parseLong(str);
    }

    public static long formatNotificationDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM_A, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(YYYY_MMDD_HHMMSS);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.parseLong(str);
    }

    public static long formatDatemDDMMYYYYhhmmaint(String inputDate, String input) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(YYYY_MMDD_HHMMSS);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.parseLong(str);
    }

    public static long formatDatemDDMMYYYYint(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(YYYY_MMDD_HHMMSS);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.parseLong(str);
    }

    public static long formatDatemDDMMYYYYint(Date inputDate) {
        SimpleDateFormat outputFormat = new SimpleDateFormat(YYYY_MMDD_HHMMSS);

        String str = null;

        try {
            str = outputFormat.format(inputDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.parseLong(str);
    }

    public static String formatDatemDDMMYYYYHHMMA(Date inputDate) {
        SimpleDateFormat outputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM_A);

        Date date = null;
        String str = null;

        try {
            str = outputFormat.format(inputDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatDatemDDMMYYYYAMPM(Date inputDate) {
        SimpleDateFormat outputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM_A);

        String str = null;

        try {
            str = outputFormat.format(inputDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatDatemDDMMYYYY(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY_HH_MM_A, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(DD_MM_YYYY);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatDate(String inputDate, String inputPattern, String outputPattern) {
        //Log.e("Input date", inputDate);
        //Log.e("Input pattern", inputPattern);
        //Log.e("Input outputPattern", outputPattern);
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static Date parseDate(String dateStr, String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }


    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }


    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;

    }

    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getTodayDate(String type) {
        Date d1 = new Date();
        try {
            return stringToDate(formatDate(d1, type), type);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return new SimpleDateFormat(type).format(today.getTime()).trim();
        return null;
    }


    public static Date getFirstDayOfLastWeekRange() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -7);
        Date start = c.getTime();
        return start;
    }

    public static String[] getDashboardDate(Calendar cal) {
        Date date = cal.getTime();
        String[] fullDate = new String[3];
        fullDate[0] = dateToString(date, dd);
        fullDate[1] = dateToString(date, MMM);
        fullDate[2] = dateToString(date, yyyy);
        return fullDate;
    }

    public static Date getTomorrow(Calendar calendar) {

        //Log.e("hrs", calendar.get(Calendar.HOUR_OF_DAY) + "");
        //Log.e("rem_hrs", 24 - calendar.get(Calendar.HOUR_OF_DAY) + "");

        calendar.add(Calendar.HOUR_OF_DAY, 24 - calendar.get(Calendar.HOUR_OF_DAY));
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        // cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.HOUR_OF_DAY, EndTime);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    public static Date getStartOfDay(Date day, Calendar cal) {
        if (day == null) day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, StartTime);
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
       /* cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));*/
        //
        return cal.getTime();
    }

    public static Date parseDateToDate(String inputDate, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;

        try {
            date = outputFormat.parse(inputFormat.format(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
