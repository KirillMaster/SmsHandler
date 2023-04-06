package com.example.smshandler;

public class Sms {
    public long TimeStamp;
    public String Text;
    public String From;

    public String Id;

    public Sms(long timeStamp,String text, String from){
        this.TimeStamp = timeStamp;
        this.Text = text;
        this.From = from;

        String toHash = this.TimeStamp + this.From + this.Text;
        this.Id  = Extensions.Sha256(toHash);
    }
}
