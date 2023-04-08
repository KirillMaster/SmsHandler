package com.example.smshandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent serviceIntent = new Intent(context, ForegroundService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent);
                }
                else{
                    context.startService(serviceIntent);
                }
            }
        }
        catch (Exception ex){
            Log.e("BroadCastReceiver", "Can't broadcast actions");
        }
    }
}