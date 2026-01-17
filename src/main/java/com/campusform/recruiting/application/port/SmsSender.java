package com.campusform.recruiting.application.port;

public interface SmsSender {

    //void sendPassNotification(String phone, String pass);

    //void sendFailNotification(String phone, String pass);
    void sendSms(String phoneNumber, String content);
}
