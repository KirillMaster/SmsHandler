package com.example.smshandler;

import static com.example.smshandler.GlobalConfiguration.CheckForMessagesTimeoutMs;
import static com.example.smshandler.GlobalConfiguration.HealthCheckTimeoutMs;

import android.util.Log;

import com.pengrad.telegrambot.TelegramBot;

public class SmsHandlerJob {
    public void RunJob(){
        int counter = 0;
        TelegramBot bot = TelegramBotService.GetBot();
        TelegramBotService.SendMessage("Job has been started successfully", bot);

        while (true) {
            Log.e("Service", "Service is running...");
            try {

                Thread.sleep(CheckForMessagesTimeoutMs);
                TelegramBotService.SendUnseenMessages(1, bot);

                //30 minutes
                if(counter > HealthCheckTimeoutMs){
                    counter = 0;
                }

                if(counter == 0){
                    TelegramBotService.SendMessage("Health check", bot);
                }
                counter+=CheckForMessagesTimeoutMs;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
