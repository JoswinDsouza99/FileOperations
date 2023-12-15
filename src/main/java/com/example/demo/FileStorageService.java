package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.DeflaterOutputStream;

@Service
public class FileStorageService {

    @Autowired
    private TestRepository testRepository;


    public FileModel storeFile(MultipartFile file) throws Exception {

        byte[] fileData = file.getBytes();

        byte[] encryptData = encryptData(fileData);

        byte[] compressData = compressData(encryptData);

        FileModel fileModel = new FileModel();
        fileModel.setFilename(file.getOriginalFilename());
        fileModel.setFilesize(String.valueOf(file.getSize()));
        fileModel.setData(compressData);
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

    public void deleteFileById(Long id) {
        testRepository.deleteById(id);
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

    byte[] encryptData(byte[] data) throws Exception {
        // Generate or retrieve your secret key
        SecretKey secretKey = generateSecretKey();

                Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(data);
    }

    public SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    byte[] compressData(byte[] data) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DeflaterOutputStream dos = new DeflaterOutputStream(baos)) {
            dos.write(data);
            dos.close();
            return baos.toByteArray();
        }
    }

}

