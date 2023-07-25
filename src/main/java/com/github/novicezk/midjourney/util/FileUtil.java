package com.github.novicezk.midjourney.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Log4j2
@Service
public class FileUtil {

    public String downloadFile(String fileUrl, String localFilePath) {
        try {
            log.info("downloadFile {} {}", fileUrl,  localFilePath);
            URL url = new URL(fileUrl);
            String fileName = getFileNameFromUrl(url);
            if(fileName.endsWith(".webp")){
                fileName = UUID.randomUUID() + fileName;
            }
            String destinationPath = localFilePath + File.separator + fileName;

            File localFile = new File(destinationPath);

            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }

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

