package com.example.florify.helpers;

import com.example.florify.models.DateRangeItems;

import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {

    public static long LASTHOUR = 3600000L;
    public static long LASTDAY = 86400000L;
    public static long LASTWEEK = 604800000L;
    public static long LASTMONTH = 2592000000L;
    public static long LASTYEAR = 30758400000L;

    public static String getDateFromMiliseconds(long milis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milis);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.toString(mDay) + "." + Integer.toString(mMonth) + "." + Integer.toString(mYear) + ".";
    }

    public static long getMilisecondsByDateRangeItem(DateRangeItems item) {
        long currentMiliseconds = new Date().getTime();
        long timePeriodMiliseconds = 0;
        switch (item) {
            case LAST_HOUR : {
                timePeriodMiliseconds = LASTHOUR;
                break;
            }
            case LAST_DAY:  {
                timePeriodMiliseconds = LASTDAY;
                break;
            }
            case LAST_WEEK: {
                timePeriodMiliseconds = LASTWEEK;
                break;
            }
            case LAST_MONTH : {
                timePeriodMiliseconds = LASTMONTH;
                break;
            }
            case LAST_YEAR : {
                timePeriodMiliseconds = LASTYEAR;
                break;
            }
            case ANYTIME: {
                return 0;
            }
        };
        return currentMiliseconds - timePeriodMiliseconds;
    }
}
