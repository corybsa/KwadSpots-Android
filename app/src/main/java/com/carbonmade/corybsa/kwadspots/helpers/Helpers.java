package com.carbonmade.corybsa.kwadspots.helpers;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import java.util.Date;
import java.util.Locale;

public class Helpers {
    public static String getDateDifference(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if(elapsedDays > 1) {
            return String.format(Locale.US, "%d days ago", elapsedDays);
        } else if(elapsedDays == 1) {
            return String.format(Locale.US, "%d day ago", elapsedDays);
        }

        if(elapsedHours > 1) {
            return String.format(Locale.US, "%d hours ago", elapsedHours);
        } else if(elapsedHours == 1) {
            return String.format(Locale.US, "%d hour ago", elapsedHours);
        }

        if(elapsedMinutes > 1) {
            return String.format(Locale.US, "%d minutes ago", elapsedMinutes);
        } else if(elapsedMinutes == 1) {
            return String.format(Locale.US, "%d minute ago", elapsedMinutes);
        }

        if(elapsedSeconds > 1) {
            return String.format(Locale.US, "%d seconds ago", elapsedSeconds);
        } else {
            return String.format(Locale.US, "%d second ago", elapsedSeconds);
        }
    }

    public static void showAlert(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }
}
