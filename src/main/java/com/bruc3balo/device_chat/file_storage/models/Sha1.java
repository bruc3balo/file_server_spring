package com.bruc3balo.device_chat.file_storage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Sha1(String hash) {

    public Sha1(String hash) {
        this.hash = hash;
        assert hash.length() == 40;
    }

    @JsonIgnore
    public String getHashSubFolder() {
        return hash.substring(0, 2);
    }

    @JsonIgnore
    public String getShortHash() {
        return hash.substring(0, 6);
    }
}
