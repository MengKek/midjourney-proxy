package com.github.novicezk.midjourney.util;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class FileUtil {

    public String downloadFile(String fileUrl, String localFilePath) {
        try {
            URL url = new URL(fileUrl);
            String fileName = getFileNameFromUrl(url);
            String destinationPath = localFilePath + File.separator + fileName;

            InputStream inputStream = url.openStream();
            FileOutputStream outputStream = new FileOutputStream(destinationPath);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            System.out.println("File downloaded successfully to: " + destinationPath);
            return destinationPath;
        } catch (IOException e) {
            System.err.println("Error downloading the file: " + e.getMessage());
        }
        return null;
    }

    private String getFileNameFromUrl(URL url) {
        String fileUrl = url.getFile();
        return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
    }


}

