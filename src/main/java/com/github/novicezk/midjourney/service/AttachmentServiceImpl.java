package com.github.novicezk.midjourney.service;


import com.alibaba.fastjson.JSON;
import com.github.novicezk.midjourney.exception.AierException;
import com.github.novicezk.midjourney.exception.ErrorCodeEnum;
import com.github.novicezk.midjourney.request.AttachmentRequest;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private static String bucketName = "shouer-1317914433";
    private COSClient cosClient;
    private TransferManager transferManager;


    @Override
    public String upload(AttachmentRequest attachmentRequest, File file) {
        if (Objects.isNull(file) || !file.exists() || file.length() == 0) {
            throw new AierException(ErrorCodeEnum.PARAM_CHECK_ERROR.getCode(), "file is empty.");
        }
        log.info("upload param = {}, fileName = {}", JSON.toJSONString(attachmentRequest), file.getName());
        initClient();

        try (InputStream is = new FileInputStream(file)) {
            String key;
            if (StringUtils.isBlank(attachmentRequest.getAddress())) {
                key = "aier-images/" + renameWithUUID(file.getName());
            } else {
                key = attachmentRequest.getAddress() + "/" + renameWithUUID(file.getName());
            }

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.length());
            objectMetadata.setContentType(getMimeType(key));

            cosClient.putObject(bucketName, key, is, objectMetadata);

            String s = "https://" + bucketName + ".cos.ap-chongqing.myqcloud.com/" + key;
            log.info("返回链接： {}", s);
            return s;
        } catch (IOException e) {
            log.error("upload error. param = {}, e = {}", JSON.toJSONString(attachmentRequest), e.getStackTrace());
            throw new AierException(ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode(), ErrorCodeEnum.INTERNAL_SERVER_ERROR.getDesc());
        }
    }

    public String renameWithUUID(String filePath) throws InvalidPathException {
        Path path = Paths.get(filePath);
        String extension = getFileExtension(path);

        // Generate a new UUID as the new filename
        String newFileName = UUID.randomUUID().toString() + "." + extension;

        // Combine the new filename with the parent path
        log.info("renameWithUUID {}", newFileName);
        return newFileName;
    }

    private String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getMimeType(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String contentType = Files.probeContentType(path);

        if (contentType == null) {
            // Fallback to a default (this is not always accurate)
            contentType = "application/octet-stream";
        }
        log.info("getMimeType {}", contentType);
        return contentType;
    }

    private void initClient() {
        String secretId = "AKIDiT8cuzqW1KVYUM8wynV2a4PZYMyPTWRH";
        String secretKey = "kQwKySxHbjgXGyseHivukVBQTN7h8SbW";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-chongqing");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        this.cosClient = new COSClient(cred, clientConfig);

        // 高级接口传输类
        // 线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        transferManager = new TransferManager(cosClient, threadPool);
        // 设置高级接口的分块上传阈值和分块大小为10MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(10 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(10 * 1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);
    }

}
