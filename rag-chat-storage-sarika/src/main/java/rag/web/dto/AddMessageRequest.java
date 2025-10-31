package rag.web.dto;

public class AddMessageRequest {
    public String sender;   // user or assistant
    public String content;
    public Object context;  // any JSON-able data
}
