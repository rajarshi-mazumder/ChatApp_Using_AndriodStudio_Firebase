package com.app1.chatapp;

public class Message {
    private String sender;
    private String receiver;
    private String contact;

    public Message() {
    }

    public Message(String sender, String receiver, String contact) {
        this.sender = sender;
        this.receiver = receiver;
        this.contact = contact;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
