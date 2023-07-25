package com.github.novicezk.midjourney.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.novicezk.midjourney.request.AttachmentRequest;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Log4j2
@Service
public class CosServiceImpl implements CosService {

    private static final String BASE_URL = "https://www.aigczhi.cn/api/attachment";
//    private static final String BASE_URL = "http://localhost:10521/api/attachment";

    public String uploadFile(File file, AttachmentRequest attachmentRequest) {
        OkHttpClient client = new OkHttpClient();

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
            Response response = client.newCall(request).execute();
            JSONObject entries ;
            if (response.isSuccessful() && response.body() != null) {
                String string = response.body().string();
                entries = JSONUtil.parseObj(string);
                System.out.println("上传成功！");
                System.out.println("服务器返回的数据：" + string);
            } else {
                System.out.println("上传失败！" + JSONUtil.toJsonStr(response));
                return null;
            }
            return entries.get("message").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
