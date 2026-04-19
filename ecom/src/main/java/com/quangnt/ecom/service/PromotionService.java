package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.PromotionCreateRequest;
import com.quangnt.ecom.dto.PromotionResponse;
import com.quangnt.ecom.dto.PromotionUpdateRequest;
import com.quangnt.ecom.entity.Promotion;
import com.quangnt.ecom.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionResponse create(PromotionCreateRequest request) {
        Promotion promotion = Promotion.builder()
                .code(request.getCode())
                .discountType(Promotion.DiscountType.valueOf(request.getDiscountType()))
                .discountValue(request.getDiscountValue())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .maxUsage(request.getMaxUsage())
                .usedCount(request.getUsedCount())
                .status(Promotion.PromotionStatus.valueOf(request.getStatus()))
                .createdAt(LocalDateTime.now())
                .build();
        Promotion saved = promotionRepository.save(promotion);
        return mapToResponse(saved);
    }

    public PromotionResponse update(Integer id, PromotionUpdateRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        promotion.setCode(request.getCode());
        promotion.setDiscountType(Promotion.DiscountType.valueOf(request.getDiscountType()));
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setMaxUsage(request.getMaxUsage());
        promotion.setUsedCount(request.getUsedCount());
        promotion.setStatus(Promotion.PromotionStatus.valueOf(request.getStatus()));
        Promotion saved = promotionRepository.save(promotion);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        promotionRepository.deleteAllById(ids);
    }

    public PromotionResponse getOne(Integer id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(promotion);
    }

    public Page<PromotionResponse> search(Pageable pageable) {
        return promotionRepository.findAll(pageable).map(this::mapToResponse);
    }

    private PromotionResponse mapToResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .id(promotion.getId())
                .code(promotion.getCode())
                .discountType(String.valueOf(promotion.getDiscountType()))
                .discountValue(promotion.getDiscountValue())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .maxUsage(promotion.getMaxUsage())
                .usedCount(promotion.getUsedCount())
                .status(String.valueOf(promotion.getStatus()))
                .createdAt(promotion.getCreatedAt())
                .build();
    }
}
