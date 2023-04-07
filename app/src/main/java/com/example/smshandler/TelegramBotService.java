package com.example.smshandler;

import android.util.ArraySet;
import android.util.Log;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelegramBotService
{
    private static TelegramBot bot;
    public static Set<String> ChatIdsSet = new ArraySet<>();

    private static SmsService smsService;

    public TelegramBotService(SmsService smsService){
        TelegramBotService.smsService = smsService;
    }

    public static TelegramBot GetBot(){
        return bot;
    }

    public void Init(){
        bot = new TelegramBot(BotToken.Token);
        bot.setUpdatesListener(updates -> {

            try {


                Update update = updates.get(0);

                long chatId = update.message().chat().id();
                String userMessage = update.message().text();

                if (!ChatIdsSet.contains(Long.toString(chatId))) {
                    ChatIdsSet.add(Long.toString(chatId));
                }

                String[] responseMessages = GetResponseMessages(userMessage);

                for (String responseMessage : responseMessages) {
                    SendResponse response = bot
                            .execute(new SendMessage(chatId, responseMessage));
                }
            }
            catch (Exception ex){
                Log.e("unhandled exception on bot init", "sorry");
            }

            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public static void SendUnseenMessages(int count, TelegramBot bot){
       String[] arr = Extensions.GetAllSmsText(smsService.GetUnseenSms(count));
        for (String sms: arr){
            SendMessage(sms, bot);
        }
    }

    private static String[] GetLatestMessages(int count){
        return Extensions.GetAllSmsText(smsService.GetLatestSms(count));
    }

    private String[] GetResponseMessages(String userMessage)
    {
        if(userMessage.contains("ping")){
            return new String[]{"pong"};
        }
        if(userMessage.contains("latest")){
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(userMessage);
            int count = 1;
            while(m.find()) {
                if(m.group(0) != null){
                    count = Integer.parseInt(Objects.requireNonNull(m.group(0)));
                }
            }
            return GetLatestMessages(count);
        }
        return new String[]{userMessage};
    }

    public static void SendMessage(String text, TelegramBot bot){
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
