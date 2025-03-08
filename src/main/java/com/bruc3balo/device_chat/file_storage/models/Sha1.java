package com.bruc3balo.device_chat.file_storage.models;

public record Sha1(String hash) {

    public Sha1(String hash) {
        this.hash = hash;
        assert hash.length() == 40;
    }

    public String getHashSubFolder() {
        return hash.substring(0, 2);
    }

    public String getShortHash() {
        return hash.substring(0, 6);
    }
}
