package com.multi.blogging.multiblogging.imageUpload.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.multi.blogging.multiblogging.imageUpload.domain.ImageFile;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AwsS3ImageUploadService implements ImageUploadService {

//    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Override
    public String uploadFile(ImageFile imageFile) {
        return null;
    }
}
