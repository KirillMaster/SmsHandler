package com.example.smshandler;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Context context;

    private SmsService smsService;


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

        TelegramBot bot = new TelegramBot("6188071284:AAEBHmsthgv4xojQ03NE-TV4WZVIjW2yr4I");

// Register for updates
        bot.setUpdatesListener(updates -> {

            Update update = updates.get(0);

            long chatId = update.message().chat().id();
            SendResponse response = bot.execute(new SendMessage(chatId, "Hello!"));
            // ... process updates
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

    private void FileListing(Sms[] someSms){
        textView.setText(GetText(someSms));
    }

    private void RequestPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
    }

    public void Read_SMS(View view){
        Sms[] latestSms = smsService.GetAndStoreLatestSms();
        FileListing(latestSms);
    }

    private String GetText(Sms[] allSms){
        String result = "";
        for(int i = 0; i < allSms.length;i ++){
            Sms sms = allSms[i];
            result += "From: " + sms.From + "\r\n" +
                    "TimeStamp: " + sms.TimeStamp + "\r\n" +
                    "Text: " + sms.Text + "\r\n" +
                    "Id: " + sms.Id + "\r\n";
        }
        return result;
    }
}