package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.FileUploadRequest;
import com.quangnt.ecom.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/medias")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ResponseDto<Object>> upload(@ModelAttribute FileUploadRequest request){
        return mediaService.upload(request);
    }

    @DeleteMapping
    ResponseEntity<ResponseDto<Object>> deletes(@RequestBody List<String> ids){
        mediaService.deletes(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @Scheduled(cron = "0 0 */3 * * *")
    @GetMapping("/remove-un-media")
    public void removeUnusedMedia() {
        mediaService.removeUnusedMedia();
    }
}
