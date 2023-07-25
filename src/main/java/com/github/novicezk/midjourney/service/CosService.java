package com.github.novicezk.midjourney.service;

import com.github.novicezk.midjourney.request.AttachmentRequest;

import java.io.File;

public interface CosService {
     String uploadFile(File file, AttachmentRequest attachmentRequest);

}
