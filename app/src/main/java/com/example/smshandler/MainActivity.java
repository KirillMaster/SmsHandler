package com.example.smshandler;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Context context;

    private SmsFileService smsFileService;
    private PhoneSmsService phoneSmsService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App();
    }

    private void App(){
        context = getApplicationContext();
        textView = findViewById(R.id.textView);
        smsFileService = new SmsFileService(context);
        phoneSmsService = new PhoneSmsService(getContentResolver());
        RequestPermissions();
        FileListing();
    }

    private void FileListing(){
        Sms[] allSmsFromFile = smsFileService.GetAllSmsFromFile();
        int length = Math.min(allSmsFromFile.length, 3);

        Sms[] someSms = Arrays.copyOfRange(allSmsFromFile, 0, length-1);
        textView.setText(GetText(someSms));
    }

    private void RequestPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);
    }

    public void Read_SMS(View view){
        Sms[] latestSms = phoneSmsService.ReadLatestSms(3);
        String content = new Gson().toJson(latestSms);
        smsFileService.WriteToFile(content);
        FileListing();
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