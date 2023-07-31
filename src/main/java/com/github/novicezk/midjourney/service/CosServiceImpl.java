package com.github.novicezk.midjourney.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.novicezk.midjourney.request.AttachmentRequest;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class CosServiceImpl implements CosService{

    private static final String BASE_URL = "https://www.aigczhi.cn/api/attachment";
//    private static final String BASE_URL = "http://localhost:10521/api/attachment";

    public String uploadFile(File file, AttachmentRequest attachmentRequest) {
        log.info("uploadFIle: fileName: {} request: {}", file.getName(), JSONUtil.toJsonStr(attachmentRequest));
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file))
                .addFormDataPart("address", attachmentRequest.getAddress())
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/upload")
                .post(requestBody)
                .build();
        try {
            log.info("uploadFile ready newCall ");
            Response response = client.newCall(request).execute();
            log.info("uploadFile newCall complete");
            JSONObject entries ;
            if (response.isSuccessful()) {
                String string = response.body().string();
                entries = JSONUtil.parseObj(string);
                log.info("上传成功！");
                log.info("服务器返回的数据：" + string);
            } else {
                log.error("上传失败！");
                return "";
            }
            log.info("uploadFile return msg: {}", entries.get("message").toString());


            if (file.exists()) {
                // 文件存在，可以删除
                if (file.delete()) {
                    log.info("文件已成功删除！");
                } else {
                    log.info("文件删除失败。");
                }
            }

            return entries.get("message").toString();
        } catch (Exception e) {
            log.error("上传失败,报错:", e);
        }
        return "";
    }
}
