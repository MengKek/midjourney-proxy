package com.github.novicezk.midjourney.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.novicezk.midjourney.request.AttachmentRequest;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class CosServiceImpl implements CosService{

    @Autowired
    private AttachmentService attachmentService;


    public String uploadFile(File file, AttachmentRequest attachmentRequest) {
        log.info("uploadFIle: fileName: {} request: {}", file.getName(), JSONUtil.toJsonStr(attachmentRequest));

        try {
            log.info("uploadFile ready newCall ");
            String url = attachmentService.upload(attachmentRequest, file);
            log.info("uploadFile newCall complete");
            if (!StringUtils.isBlank(url)) {
                log.info("上传成功！");
                log.info("服务器返回的数据：" + url);
            } else {
                log.error("上传失败！");
                return "";
            }
            log.info("uploadFile return msg: {}", url);


            if (file.exists()) {
                // 文件存在，可以删除
                if (file.delete()) {
                    log.info("文件已成功删除！");
                } else {
                    log.info("文件删除失败。");
                }
            }

            return url;
        } catch (Exception e) {
            log.error("上传失败,报错:", e);
        }
        return "";
    }
}
