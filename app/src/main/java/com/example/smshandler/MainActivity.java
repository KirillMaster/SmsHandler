package com.example.smshandler;

import static java.lang.Long.parseLong;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Context context;

    private SmsFileService smsFileService;


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
        RequestPermissions();
        FileListing();
    }

    private void FileListing(){
        Sms[] allSmsFromFile = smsFileService.GetAllSmsFromFile();
        int length = allSmsFromFile.length > 3 ? 3 : allSmsFromFile.length;

        Sms[] someSms = Arrays.copyOfRange(allSmsFromFile, 0, length-1);
        textView.setText(GetText(someSms));
    }

    private void RequestPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);
    }

    public void Read_SMS(View view){
        Uri uri =  Uri.parse("content://sms");
        Cursor cursor =  getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();

        List<Sms> allSms = new ArrayList<>();

        while(!cursor.isLast()){
            Sms sms = new Sms();
            sms.From = cursor.getString(2);
            sms.Text = cursor.getString(12);
            sms.TimeStamp = parseLong(cursor.getString(4));
            allSms.add(sms);
            cursor.moveToNext();
        }

        String content = new Gson().toJson(allSms);
        smsFileService.WriteToFile(content);
        FileListing();
    }

    private String GetText(Sms[] allSms){
        String result = "";
        for(int i = 0; i < allSms.length;i ++){
            Sms sms = allSms[i];
            result += "From: " + sms.From + "\r\n" +
                    "TimeStamp: " + sms.TimeStamp + "\r\n" +
                    "Text: " + sms.Text + "\r\n";
        }
        return result;
    }
}