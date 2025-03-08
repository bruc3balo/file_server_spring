package com.bruc3balo.device_chat.file_storage.service;

import com.bruc3balo.device_chat.file_storage.models.Sha1;
import com.bruc3balo.device_chat.utils.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {

    private final Path dumpPath;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public FileServiceImpl(@Value("${dump.folder}") String dumpFolder) throws IOException {
       this.dumpPath= Files.createDirectories(Path.of(dumpFolder));
    }

    @Override
    public Sha1 storeFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String hash = Hashing.computeSha1Hash(file.getBytes());
        Sha1 sha1 = new Sha1(hash);
        log.info("Storing file " + file.getOriginalFilename() + " with hash " + hash);
        Path path = dumpPath.resolve(sha1.getHashSubFolder()).resolve(sha1.hash());
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.COPY_ATTRIBUTES);
        log.info("Stored file to "+path.toAbsolutePath());
        return sha1;
    }

    @Override
    public byte[] retrieveAndDeleteFile(Sha1 sha1) throws IOException {
        Path path = dumpPath.resolve(sha1.getHashSubFolder()).resolve(sha1.hash());
        log.info("Retrieving file from " + path.toAbsolutePath());
        return Files.readAllBytes(path);
    }
}
