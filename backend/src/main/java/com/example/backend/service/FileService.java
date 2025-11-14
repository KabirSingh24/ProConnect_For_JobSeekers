package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;



@Service
public class FileService {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + File.separator + "uploads";

    public String saveFile(MultipartFile file) throws IOException {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        // Add File.separator between folder and file name
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + File.separator + fileName;

        // Save file
        file.transferTo(new File(filePath));

        // Return URL for frontend
        return "https://proconnect-for-jobseekers.onrender.com/uploads/" + fileName;
    }
}

