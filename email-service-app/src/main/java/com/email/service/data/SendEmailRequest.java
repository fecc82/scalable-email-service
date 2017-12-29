package com.email.service.data;

import cz.jirutka.validator.collection.constraints.EachPattern;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class SendEmailRequest {
    @NotNull(message = "Recipient cannot be null")
    @EachPattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}.", message = "Request contains an invalid email")
    private ArrayList<String> recipients;

    @EachPattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}.", message = "Request contains an invalid email")
    private ArrayList<String> cc;

    @EachPattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}.", message = "Request contains an invalid email")
    private ArrayList<String> bcc;

    @NotNull(message = "Sender cannot be null")
    private String sender;

    @NotNull(message = "Body cannot be null")
    private String htmlBody;

    @NotNull(message = "Title cannot be null")
    private String htmlTitle;

    public ArrayList<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

    public ArrayList<String> getCc() {
        return cc;
    }

    public void setCc(ArrayList<String> cc) {
        this.cc = cc;
    }

    public ArrayList<String> getBcc() {
        return bcc;
    }

    public void setBcc(ArrayList<String> bcc) {
        this.bcc = bcc;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}