package com.example.smshandler;

import android.content.Context;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class TelegramBotService
{
    private ChatIdService chatIdService;
    private TelegramBot bot;
    public TelegramBotService(Context context){
        chatIdService = new ChatIdService(context);
    }

    public void Init(){
        bot = new TelegramBot("6188071284:AAEBHmsthgv4xojQ03NE-TV4WZVIjW2yr4I");
        bot.setUpdatesListener(updates -> {

            Update update = updates.get(0);

            long chatId = update.message().chat().id();
            chatIdService.SaveChatId(Long.toString(chatId));
            SendResponse response = bot.execute(new SendMessage(chatId, "Hello!"));

            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void SendMessage(String text){
        String[] chatIds = chatIdService.GetChatIds();
        for (int i = 0; i < chatIds.length;i ++){
            SendResponse response = bot.execute(new SendMessage(chatIds[i], text));
        }
    }
}
