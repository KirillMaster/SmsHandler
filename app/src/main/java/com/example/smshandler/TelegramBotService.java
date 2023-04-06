package com.example.smshandler;

import android.util.ArraySet;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.Set;

public class TelegramBotService
{
    private TelegramBot bot;
    public Set<String> ChatIdsSet = new ArraySet<>();

    public void Init(){
        bot = new TelegramBot("6188071284:AAEBHmsthgv4xojQ03NE-TV4WZVIjW2yr4I");
        bot.setUpdatesListener(updates -> {

            Update update = updates.get(0);

            long chatId = update.message().chat().id();

            if(!ChatIdsSet.contains(Long.toString(chatId))){
                ChatIdsSet.add(Long.toString(chatId));
            }

            SendResponse response = bot.execute(new SendMessage(chatId, "Hello!"));

            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void SendMessage(String text){
        new Thread(new Runnable(){
            @Override
            public void run() {
                String[] ChatIdsArray = new String[ChatIdsSet.size()];
                ChatIdsSet.toArray(ChatIdsArray);

                for (String chatId : ChatIdsArray) {
                    bot.execute(new SendMessage(Long.parseLong(chatId), text));
                }
            }
        }).start();
    }
}
