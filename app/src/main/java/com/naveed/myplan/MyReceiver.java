package com.naveed.myplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");
        Log.e("MyActivity", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,AlarmService.class);
        serviceIntent.putExtra("extra", state);

        context.startService(serviceIntent);
    }
}
