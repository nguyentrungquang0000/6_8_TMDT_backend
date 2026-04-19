package com.quangnt.ecom.service.store;

import org.springframework.web.multipart.MultipartFile;

public interface StoreClient {
    String upload(MultipartFile multipartFile, String fileKey, String fileName);

    String upload(MultipartFile multipartFile, String fileKey, String fileName, boolean isPublicBucketUpload);

    void deleteByFileKey(String fileKey);

    String getPreviewUrl(String fileKey);

    String getDownloadUrl(String fileKey, String fileName);
}
