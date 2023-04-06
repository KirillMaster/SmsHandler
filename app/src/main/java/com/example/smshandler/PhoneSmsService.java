package com.example.smshandler;

import static java.lang.Long.parseLong;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class PhoneSmsService
{
    private ContentResolver contentResolver;

    public PhoneSmsService(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }
    public Sms[] ReadLatestSms(int count){
        Uri uri =  Uri.parse("content://sms");
        Cursor cursor =  contentResolver.query(uri, null, null, null, null);
        cursor.moveToFirst();

        List<Sms> allSms = new ArrayList<>();

        int i = 0;
        while(i < count || !cursor.isLast()){
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
}
