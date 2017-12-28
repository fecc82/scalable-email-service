package com.email.service.impl;

import com.email.service.api.EmailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("mailGun")
public class MailGunServiceImpl implements EmailService {
    public void send(String to, String cc, String email) {
        System.out.println("ATTEMPTING TO SEND mail gun");
    }
}
