package com.bruc3balo.device_chat.file_storage.service;

import com.bruc3balo.device_chat.file_storage.models.Sha1;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface FileService {
    Sha1 storeFile(MultipartFile file) throws IOException, NoSuchAlgorithmException;

    Sha1 storeFile(byte[] blob) throws IOException, NoSuchAlgorithmException;

    byte[] retrieveAndDeleteFile(Sha1 sha1) throws IOException;
}
