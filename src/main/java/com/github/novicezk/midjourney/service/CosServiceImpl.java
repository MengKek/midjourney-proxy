package com.github.novicezk.midjourney.service;

import com.github.novicezk.midjourney.request.AttachmentRequest;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CosServiceImpl implements CosService {
    private static final String BASE_URL = "https://www.aigczhi.cn/v1/attachment";

    public String uploadFile(File file, AttachmentRequest attachmentRequest) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file))
                .addFormDataPart("code", attachmentRequest.getCode())
                .addFormDataPart("width", String.valueOf(attachmentRequest.getWidth()))
                .addFormDataPart("height", String.valueOf(attachmentRequest.getHeight()))
                .addFormDataPart("imageSize", String.valueOf(attachmentRequest.getImageSize()))
                .addFormDataPart("address", attachmentRequest.getAddress())
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/upload")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("上传成功！");
                System.out.println("服务器返回的数据：" + response.body().string());
            } else {
                System.out.println("上传失败！错误码：" + response.code());
            }
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
