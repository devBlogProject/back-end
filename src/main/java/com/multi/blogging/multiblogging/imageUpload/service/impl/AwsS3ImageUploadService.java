package com.multi.blogging.multiblogging.imageUpload.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.multi.blogging.multiblogging.imageUpload.domain.ImageFile;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AwsS3ImageUploadService implements ImageUploadService {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Override
    public String uploadFile(MultipartFile multipartFile) {
        validateFile(multipartFile);
        var imageFile = ImageFile.from(multipartFile);

        var metadata = createObjectMetadata(multipartFile);
        try (var inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, imageFile.getStoredFileName(), inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드에 실패했습니다.");
        }
        return amazonS3.getUrl(bucket, imageFile.getStoredFileName()).toString();
    }


    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        var metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }

    private void validateFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다.");
        }
    }

}
