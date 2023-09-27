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
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            log.info("downloadFile {} {}", fileUrl,  localFilePath);
            URL url = new URL(fileUrl);
            String fileName = getFileNameFromUrl(url);
            fileName = UUID.randomUUID() + fileName;
            String destinationPath = localFilePath + File.separator + fileName;

            File localFile = new File(destinationPath);

            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }
            inputStream = url.openStream();
            outputStream = new FileOutputStream(destinationPath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            log.info("File downloaded successfully to: " + fileName);
            return fileName;
        } catch (IOException e) {
            log.error("Error downloading the file: " + e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String getFileNameFromUrl(URL url) {
        String fileUrl = url.getFile();
        return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
    }


}

