package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
public class FileStorageService {

    @Autowired
    private TestRepository testRepository;


    public FileModel storeFile(MultipartFile file) throws IOException {
        FileModel fileModel = new FileModel();
        fileModel.setFilename(file.getOriginalFilename());
        fileModel.setFilesize(String.valueOf(file.getSize()));
        fileModel.setData(file.getBytes());
        return testRepository.save(fileModel);
    }

    public FileModel getFileById(Long id) {
        return testRepository.findById(id).orElse(null);
    }
    @Transactional(readOnly = true)
    public byte[] getFileData(Long id) {
        return testRepository.findById(id).map(FileModel::getData).orElse(null);
    }

    public FileModel prepareFileForResponse(Long id) {
        FileModel file = getFileById(id);
        if (file != null) {
            //String base64Data = Base64.getEncoder().encodeToString(file.getData());
            //file.setBase64Data(base64Data);
            file.setFilesize(formatFileSize(file.getData().length));
        }
        return file;
    }

    public String getFileDataAsBase64(Long id) {
        byte[] fileData = getFileData(id);
        return Base64.getEncoder().encodeToString(fileData);
    }

    private String formatFileSize(long size) {
        // Convert bytes to MB and format the string
        double sizeInMb = size / (1024.0 * 1024.0);
        return String.format("%.2f MB", sizeInMb);
    }

}

