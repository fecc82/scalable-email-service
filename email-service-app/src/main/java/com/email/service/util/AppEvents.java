package com.email.service.util;

import com.email.service.data.SendEmailRequest;
import org.joda.time.LocalDateTime;

public class AppEvents {
    private String event;
    private LocalDateTime time;
    private SendEmailRequest payload;
    private Throwable error;

    public AppEvents(String event, SendEmailRequest payload) {
        this(event, payload, null);
    }

    public AppEvents(String event, SendEmailRequest payload, Throwable error) {
        time = LocalDateTime.now();
        this.event = event;
        this.payload = payload;
        this.error = error;
    }

    public static AppEvents eventOf(String event, SendEmailRequest payload) {
        return eventOf(event, payload, null);
    }

    public static AppEvents eventOf(String event, SendEmailRequest payload, Throwable error) {
        AppEvents appEvent = new AppEvents(event, payload, error);
        System.out.println(appEvent);
        return appEvent;
    }

    @Override
    public String toString() {
        return "AppEvents{" +
                " event= " + event +
                ", time= " + time +
                ", payload= " + payload +
                ", error= '" + error + '\'' +
                '}';
    }
}
