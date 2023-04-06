package com.example.smshandler;

import android.content.Context;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class ChatIdService {

    private String FileName = "chatIds.txt";
    private Context context;

    public ChatIdService(Context context){
        this.context = context;
    }
    public String[] GetChatIds(){
        String content = FileService.ReadFromFile(FileName, context);
        return new Gson().fromJson(content, String[].class);

    }

    public boolean SaveChatId(String chatId){
        String[] chatIdsArr = GetChatIds();
        List<String> chatIds = Arrays.asList(chatIdsArr);
        boolean isNew = false;

        for(int i = 0; i < chatIds.size();i++){
            if(!chatIds.contains(chatId)){
                isNew = true;
                chatIds.add(chatId);
            }
        }

        FileService.WriteToFile(new Gson().toJson(chatIds), FileName, context);
        return isNew;
    }
}
