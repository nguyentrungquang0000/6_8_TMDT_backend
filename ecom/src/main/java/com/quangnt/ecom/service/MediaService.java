package com.quangnt.ecom.service;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.FileUploadRequest;
import com.quangnt.ecom.entity.Media;
import com.quangnt.ecom.repository.MediaRepository;
import com.quangnt.ecom.service.store.StoreClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {
    private final MediaRepository mediaRepository;
    private final StoreClient storeClient;

    public ResponseEntity<ResponseDto<Object>> upload(FileUploadRequest request) {
        String mediaId = String.valueOf(UUID.randomUUID());
        MultipartFile multipartFile = request.file();
        String fileName = request.file().getOriginalFilename();

        String extension = FilenameUtils.getExtension(fileName) != null
                ? FilenameUtils.getExtension(fileName).toLowerCase()
                : "";

        String newFileName = String.format("%s.%s", mediaId, extension);

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(new Date());

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        String fileKey = String.format("%d/%d/%s", year, month, newFileName);

        String url = storeClient.upload(multipartFile, fileKey, fileName);

        Media media = new Media();
        media.setId(mediaId);
        media.setName(fileName);
        media.setUrl(url);
        media.setFileKey(fileKey);
        media.setContentType(multipartFile.getContentType());
        media.setSize(multipartFile.getSize());
        media = mediaRepository.save(media);

        media.setUrl(storeClient.getPreviewUrl(fileKey));
        return ResponseBuilder.success(
                media,
                ResponseCode.SUCCESS
        );
    }

    public void deletes(List<String> ids) {
        for (String id : ids) {
            mediaRepository.findById(id)
                    .ifPresent(media -> storeClient.deleteByFileKey(media.getFileKey()));
        }
        mediaRepository.deleteAllById(ids);
    }

    public Media getMediaById(String mediaId) {
        return mediaRepository.findById(mediaId)
            .orElseThrow(() -> new BusinessException(
                    ResponseCode.ENTITY_NOT_FOUND,
                    "Media",
                    mediaId
                )
            );
    }

    public void deleteMediaById(String mediaId) {
        if (mediaId == null) {
            return;
        }
        Media media = getMediaById(mediaId);
        if (media == null){
            log.error("Media not found with id: {}", mediaId);
            return;
        }
        storeClient.deleteByFileKey(media.getFileKey());
        mediaRepository.delete(media);
    }

    public void deleteMedia(Media media) {
        if (media != null) {
            storeClient.deleteByFileKey(media.getFileKey());
            mediaRepository.delete(media);
        }
    }

    public String getPreviewUrl(String mediaId) {
        if (mediaId == null) {
            return null;
        }
        Media media = getMediaById(mediaId);
        return storeClient.getPreviewUrl(media.getFileKey());
    }

    public Map<String, String> getPreviewUrls(List<String> mediaIds) {
        if (mediaIds == null || mediaIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Media> mediaList = mediaRepository.findByIdIn(mediaIds);
        return mediaList.stream()
                .collect(Collectors.toMap(
                        Media::getId,
                        media -> storeClient.getPreviewUrl(media.getFileKey())
                ));
    }

    public void removeUnusedMedia() {
        log.info("---RemoveUnusedMedia");
        Set<Media> mediaList = mediaRepository.findByStatusFalseAndCreatedAtBefore(Instant.now().minusSeconds(21600));
        for (Media media : mediaList) {
            try {
                deleteMedia(media);
            } catch (Exception e) {
                log.error("Cannot delete media {}", media.getId(), e);
            }
        }
    }
}
