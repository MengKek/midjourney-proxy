package com.github.novicezk.midjourney.service;

import com.github.novicezk.midjourney.request.AttachmentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AttachmentService {
    String upload(AttachmentRequest attachmentRequest, File file);
}
