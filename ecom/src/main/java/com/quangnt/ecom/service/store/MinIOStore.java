package com.quangnt.ecom.service.store;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Primary
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "store.minio")
public class MinIOStore implements StoreClient {
    @Value("${host}")
    String HOST;
    @Value("${access-key}")
    String ACCESS_KEY;
    @Value("${secret-key}")
    String SECRET_KEY;
    @Value("${public-bucket}")
    String PUBLIC_BUCKET;
    @Value("${private-bucket}")
    String PRIVATE_BUCKET;
    @Value("${presigned-url-expiration-in-minute}")
    Integer PRESIGNED_URL_EXPIRATION_IN_MINUTE;

    AmazonS3 client;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY); // tạo key để truy cập vào hệ
                                                                                      // thống aws
        client = new AmazonS3Client(credentials); // tạo kết nối đến server
        client.setEndpoint(HOST); // set endpoint url
        S3ClientOptions options = new S3ClientOptions();
        options.setPathStyleAccess(true);
        client.setS3ClientOptions(options);
    }

    @Override
    public String upload(MultipartFile multipartFile, String fileKey, String fileName) {
        return upload(multipartFile, fileKey, fileName, false);
    }

    @Override
    public String upload(MultipartFile multipartFile,
                         String fileKey,
                         String fileName,
                         boolean isPublicBucketUpload) {
        String bucket = isPublicBucketUpload ? PUBLIC_BUCKET : PRIVATE_BUCKET;

        try (InputStream is = multipartFile.getInputStream()) {

            ObjectMetadata fileMetadata = new ObjectMetadata();
            fileMetadata.setContentLength(multipartFile.getSize());
            fileMetadata.setContentType(multipartFile.getContentType());

            if (fileName != null) {
                fileMetadata.setContentDisposition(
                    String.format(
                        "inline; filename*=UTF-8''%s",
                        URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                );
            }

            PutObjectRequest putRequest = new PutObjectRequest(
                bucket,
                fileKey,
                is,
                fileMetadata
            );

            client.putObject(putRequest);
        } catch (IOException e) {
            throw new RuntimeException("Upload file failed", e);
        }

        return String.format("%s/%s/%s", HOST, bucket, fileKey);
    }

    public String upload(MultipartFile file, String fileKey, boolean isPublicBucketUpload) {
        try (InputStream is = file.getInputStream()) {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest request = new PutObjectRequest(isPublicBucketUpload ? PUBLIC_BUCKET : PRIVATE_BUCKET,
                fileKey,
                is,
                metadata);

            client.putObject(request);

            return String.format("%s/%s/%s", HOST, PRIVATE_BUCKET, fileKey);

        } catch (Exception e) {
            throw new RuntimeException("Upload file failed", e);
        }
    }


    @Override
    public void deleteByFileKey(String fileKey) {
        try {
            if (StringUtils.isNullOrEmpty(fileKey)) {
                return;
            }
            client.deleteObject(PRIVATE_BUCKET, fileKey.trim());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi hệ thống khi xoá file", e);
        }
    }

    @Override
    public String getPreviewUrl(String fileKey) {
        if (StringUtils.isNullOrEmpty(fileKey)) {
            return null;
        }

        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        String extension = FilenameUtils.getExtension(fileKey);
        switch (extension.toLowerCase()) {
            case "pdf":
                responseHeaders.setContentType(MediaType.APPLICATION_PDF_VALUE);
                break;
            case "jpg":
            case "jpeg":
                responseHeaders.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case "png":
                responseHeaders.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
            case "mp4":
                responseHeaders.setContentType("video/mp4");
                break;
            case "docx":
                responseHeaders.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                break;
        }

        responseHeaders.setContentDisposition("inline");

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();

        expTimeMillis += PRESIGNED_URL_EXPIRATION_IN_MINUTE != null
                ? 1000L * 60 * PRESIGNED_URL_EXPIRATION_IN_MINUTE
                : 1000L * 60 * 5;

        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(PRIVATE_BUCKET, fileKey)
                        .withMethod(HttpMethod.GET)
                        .withResponseHeaders(responseHeaders)
                        .withExpiration(expiration);

        return client.generatePresignedUrl(request).toString();
    }

    @Override
    public String getDownloadUrl(String fileKey, String fileName) {
        if (StringUtils.isNullOrEmpty(fileKey)) {
            return null;
        }

        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        String extension = FilenameUtils.getExtension(fileKey);
        if (extension != null) {
            switch (extension.toLowerCase()) {
                case "pdf":
                    responseHeaders.setContentType(MediaType.APPLICATION_PDF_VALUE);
                    break;
                case "jpg":
                case "jpeg":
                    responseHeaders.setContentType(MediaType.IMAGE_JPEG_VALUE);
                    break;
                case "png":
                    responseHeaders.setContentType(MediaType.IMAGE_PNG_VALUE);
                    break;
                case "mp4":
                    responseHeaders.setContentType("video/mp4");
                    break;
                case "docx":
                    responseHeaders.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    break;
                default:
                    responseHeaders.setContentType("application/octet-stream");
            }
        }

        responseHeaders.setContentDisposition("attachment; filename=\"" + fileName + "\"");

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();

        expTimeMillis += PRESIGNED_URL_EXPIRATION_IN_MINUTE != null
                ? 1000L * 60 * PRESIGNED_URL_EXPIRATION_IN_MINUTE
                : 1000L * 60 * 10;

        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(PRIVATE_BUCKET, fileKey)
                        .withMethod(HttpMethod.GET)
                        .withResponseHeaders(responseHeaders)
                        .withExpiration(expiration);

        return client.generatePresignedUrl(request).toString();
    }
}
