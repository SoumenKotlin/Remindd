package com.app.remindd.activities;

/**
 * Created by rishav on 7/12/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, LocationService.class);
        context.startService(service);

    }

}