package com.example.smshandler;

import android.util.ArraySet;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TelegramBotService
{
    private TelegramBot bot;
    public Set<String> ChatIdsSet = new ArraySet<>();

    private SmsService smsService;

    public TelegramBotService(SmsService smsService){
        this.smsService = smsService;
    }

    public void Init(){
        bot = new TelegramBot("6188071284:AAEBHmsthgv4xojQ03NE-TV4WZVIjW2yr4I");
        bot.setUpdatesListener(updates -> {

            Update update = updates.get(0);

            long chatId = update.message().chat().id();
            String userMessage = update.message().text();

            if(!ChatIdsSet.contains(Long.toString(chatId))){
                ChatIdsSet.add(Long.toString(chatId));
            }

            String[] responseMessages = GetResponseMessages(userMessage);

            for(String responseMessage: responseMessages){
                SendResponse response = bot
                        .execute(new SendMessage(chatId, responseMessage));
            }

            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private String[] GetResponseMessages(String userMessage)
    {
        if(userMessage.contains("ping")){
            return new String[]{"pong"};
        }
        if(userMessage.contains("test ")){

            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(userMessage);
            int count = 1;
            while(m.find()) {
                if(m.group(0) != null){
                    count = Integer.parseInt(Objects.requireNonNull(m.group(0)));
                }
            }
            List<String> smsList =  Arrays.stream(smsService.GetLatestSms(count))
                    .map(Extensions::GetSmsText)
                    .collect(Collectors.toList());

            String[] arr = new String[smsList.size()];

            return smsList.toArray(arr);
        }
        return new String[]{userMessage};
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
