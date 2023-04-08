package com.example.smshandler;

import static com.example.smshandler.GlobalConfiguration.FileNameForStoredSms;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;

public class SmsFileService {
    private final Context context;
    public SmsFileService(Context context){
        this.context = context;
    }

    public void WriteToFile(String data) {
        try{
            FileService.WriteToFile(data,  FileNameForStoredSms, context);
        }
        catch (Exception ex){
            Log.e("fileService", "Cant' write to file");
        }
    }

    public Sms[] GetAllSmsFromFile(){
        Sms[] allSms = new Sms[0];
        try{
            String fileContent = readFromFile();
            allSms = new Gson().fromJson(fileContent, Sms[].class);
            if(allSms == null){
                allSms = new Sms[0];
            }
            Arrays.sort(allSms, (a,b) -> a.TimeStamp < b.TimeStamp ? 1 : -1);
            }
        catch (Exception ex){
            Log.e("fileService", "cannot't get all sms from file");
        }
        return allSms;
    }

    private String readFromFile() {
        try{
            return FileService.ReadFromFile(FileNameForStoredSms, context);
        }
        catch (Exception ex){
            Log.e("FileService", "can't read from file");
        }
        return "";
    }
}
