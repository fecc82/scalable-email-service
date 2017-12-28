package com.email.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("sendGrid")
public class SendGridServiceImpl {
    public void send(String to, String cc, String email) {
        System.out.println("ATTEMPTING TO send grid");
    }
}
