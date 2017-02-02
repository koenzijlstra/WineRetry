package com.example.koen.wineretry.Objects;

import java.util.Date;

/**
 * This is a model for a message. It contains the message itself, the user that sent it and a long
 * of the timestamp. It is created when user presses the send button, and is used for displaying all
 * the messages in a chat
 */

//

public class ChatMessageObject {

    private String messageText;
    private String messageUser;
    private long messageTime;

    public ChatMessageObject(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessageObject(){
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
