package com.example.smshandler;

import android.content.Context;

import com.google.gson.Gson;

import java.util.Arrays;

public class SmsFileService {

    private String FileName = "5.txt";
    private Context context;

    public SmsFileService(Context context){
        this.context = context;
    }

    public void WriteToFile(String data) {
        FileService.WriteToFile(data, FileName, context);
    }

    public Sms[] GetAllSmsFromFile(){
        String fileContent = readFromFile();
        Sms[] allSms = new Gson().fromJson(fileContent, Sms[].class);
        if(allSms == null){
            allSms = new Sms[0];
        }
        Arrays.sort(allSms, (a,b) -> a.TimeStamp < b.TimeStamp ? 1 : -1);
        return allSms;
    }

    private String readFromFile() {
        return FileService.ReadFromFile(FileName, context);
    }
}
