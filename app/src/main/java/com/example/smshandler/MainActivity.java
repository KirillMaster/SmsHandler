package com.example.smshandler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onStart(){
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App();
    }

    @SuppressLint("SetTextI18n")
    private void App(){
        Context context = getApplicationContext();
        SmsFileService smsFileService = new SmsFileService(context);
        PhoneSmsService phoneSmsService = new PhoneSmsService(getContentResolver());
        SmsService smsService = new SmsService(phoneSmsService, smsFileService);
        RequestPermissions();

        Log.e("Foreground service running?", foregroundServiceRunning() ? "Yes" : "No");

        if(!foregroundServiceRunning()){
            TelegramBotService telegramBotService = new TelegramBotService(smsService);
            telegramBotService.Init();
            StartForegroundJob();
        }
        TextView textView = findViewById(R.id.textView);

        if(foregroundServiceRunning()){
            textView.setText("Прослушка сообщений работает. \r\n" +
                    "Это значит, что можно нажать кнопку закрыть\r\n" +
                    "и пользоваться телефоном как обычно. \r\n " +
                    "Прослушка будет работать в фоновом режиме \r\n" +
                    "Если телефон выключится, то запусти меня опять после включения телефона\r\n");
        }
        else{
            textView.setText("Не работает");
        }
    }

    private void StartForegroundJob(){
        try{
            Intent serviceIntent = new Intent(this, ForegroundService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            }
            else {
                startService(serviceIntent);
            }

            foregroundServiceRunning();
        }
        catch (Exception ex){
            Log.e("Activity", "Can't run foreground task");
        }
    }

    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(ForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void RequestPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.BROADCAST_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECEIVE_BOOT_COMPLETED}, PackageManager.PERMISSION_GRANTED);
    }

    public void CloseApp(View view){
        finish();
    }
}