package com.example.smshandler;

import static java.lang.Long.parseLong;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PhoneSmsService
{
    private final ContentResolver contentResolver;

    public PhoneSmsService(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }
    public Sms[] ReadLatestSms(int count){
        try{
            Uri uri =  Uri.parse("content://sms");
            Cursor cursor =  contentResolver.query(uri, null, null, null, null);
            cursor.moveToFirst();

            List<Sms> allSms = new ArrayList<>();

            int i = 0;
            while(i < count && !cursor.isLast()){
                Sms sms = new Sms(
                        parseLong(cursor.getString(4)), //timestamp
                        cursor.getString(12), //text
                        cursor.getString(2) ); //from
                allSms.add(sms);
                cursor.moveToNext();
                i++;
            }

            Sms[] result = new Sms[allSms.size()];
            return allSms.toArray(result);
        }
        catch (Exception ex){
            Log.e("PhoneSmsService", "Can't read latest Sms");
            return new Sms[0];
        }
    }
}
