package com.app.remindd.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rishav on 23/11/16.
 */

public class Utils {

    public static String getDateTime() {

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(Calendar.getInstance().getTime());

    }

    static double milesToMeter(double i) {
        return i*1609.34;
    }

    static double kmToMeter(double i) {
        return i*1000;
    }
}
