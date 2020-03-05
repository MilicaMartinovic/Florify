package com.example.florify.helpers;

import java.util.Calendar;

public class DateTimeHelper {

    public String getDateFromMiliseconds(long milis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milis);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.toString(mDay) + "." + Integer.toString(mMonth) + "." + Integer.toString(mYear) + ".";
    }
}
