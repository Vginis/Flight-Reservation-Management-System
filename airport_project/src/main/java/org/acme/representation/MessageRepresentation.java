package org.acme.representation;

public class MessageRepresentation {
    private String key;

    public MessageRepresentation() {
    }

    public MessageRepresentation(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
