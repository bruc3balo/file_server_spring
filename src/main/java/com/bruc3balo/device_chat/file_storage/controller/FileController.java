package com.bruc3balo.device_chat.file_storage.controller;

import com.bruc3balo.device_chat.file_storage.models.Sha1;
import com.bruc3balo.device_chat.file_storage.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private final FileService fileService;
    private final Logger log = Logger.getLogger(FileController.class.getName());

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Sha1 sha1 = fileService.storeFile(file);
            return ResponseEntity.ok().body(sha1);
        } catch (Exception e) {
            log.severe(e.getMessage());
            log.severe(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("exception", e.getMessage());
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to upload file. Please try again later.");
            return ResponseEntity.internalServerError().body(errorResponse);
        }

    }

    @GetMapping(value = "download/{sha1}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadFile(@PathVariable String sha1) {
        try {
            byte[] data = fileService.retrieveAndDeleteFile(new Sha1(sha1));
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + sha1 + "\"")
                    .body(data);
        } catch (IOException e) {
            log.severe(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.severe(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
            return ResponseEntity.internalServerError().build();
        }
    }

}
