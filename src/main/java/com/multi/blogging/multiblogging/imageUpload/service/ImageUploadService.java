package com.multi.blogging.multiblogging.imageUpload.service;

import com.multi.blogging.multiblogging.imageUpload.domain.ImageFile;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    String uploadFile(MultipartFile multipartFile);
}
