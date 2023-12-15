package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class TestController {
    private final FileStorageService fileStorageService;

    @Autowired
    public TestController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file) {
        try {
            FileModel fileModel = fileStorageService.storeFile(file);
            String fileDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(fileModel.getId().toString())
                    .toUriString();

            return ResponseEntity.ok("File uploaded successfully: " + fileDownloadUrl);

        } catch (IOException e) {
            return ResponseEntity.ok("Could not upload the file!");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*@GetMapping("noData/{id}")
    public ResponseEntity<FileModel> getFile(@PathVariable Long id) {
        return fileStorageService.getFileById(id)
                .map(file -> {
                    file.setData(null); // Ensure data is not serialized
                    return ResponseEntity.ok(file);
                })
                .orElse(ResponseEntity.notFound().build());
    }*/

    // Endpoint to get file data
    @GetMapping("/{id}/data")
    public ResponseEntity<byte[]> getFileData(@PathVariable Long id) {
        byte[] data = fileStorageService.getFileData(id);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<FileModel> getFile(@PathVariable Long id) {
        FileModel file = fileStorageService.prepareFileForResponse(id);
        if (file != null) {
            return ResponseEntity.ok(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value ="/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        try {
            fileStorageService.deleteFileById(id);
            return ResponseEntity.ok("Deleted Successfully!");
        }
        catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String formatFileSize(long size) {
        // Convert bytes to MB and format the string
        double sizeInMb = size / (1024.0 * 1024.0);
        return String.format("%.2f MB", sizeInMb);
    }
}
