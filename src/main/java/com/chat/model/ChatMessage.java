package com.chat.model;

public class ChatMessage {
    private String content;
    private String sender;
    private String receiver;
    private MessageType type;
    private String timestamp;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    // Default constructor
    public ChatMessage() {}

    // Constructor with parameters
    public ChatMessage(String content, String sender, String receiver, MessageType type) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
