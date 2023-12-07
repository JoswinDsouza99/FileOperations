package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageService {

    @Autowired
    private TestRepository testRepository;


    public FileModel storeFile(MultipartFile file) throws IOException {
        FileModel fileModel = new FileModel();
        fileModel.setFilename(file.getOriginalFilename());
        fileModel.setFilesize(file.getSize());
        fileModel.setData(file.getBytes());
        return testRepository.save(fileModel);
    }
}

