package com.multi.blogging.multiblogging.imageUpload.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ImageFile {
    String originalFileName;
    String storedFileName;
    MultipartFile multipartFile;

    static public ImageFile from(MultipartFile multipartFile){
        return new ImageFile(multipartFile.getOriginalFilename(),
                createStoreFileName(multipartFile.getOriginalFilename()),
                multipartFile
        );
    }

    /*
     - 이미지 중복 방지용 무작위 파일 이름 생성기
     */
    private static String createStoreFileName(String originalFilename) {
        var ext = extractExt(originalFilename);
        var uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    /*
        - 파일 이름 추출
    */
    private static String extractExt(String originalFilename) {
        var pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}