package com.ceos22.springcgv.controller.purchase;

import com.ceos22.springcgv.config.CustomUserDetails;
import com.ceos22.springcgv.dto.purchase.PurchaseRequestDto;
import com.ceos22.springcgv.global.response.ApiResponse;
import com.ceos22.springcgv.service.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * 매점 상품 구매
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody PurchaseRequestDto requestDto) {

        Long currentUserId = userDetails.getUserId();
        Long purchaseId = purchaseService.createPurchase(currentUserId, requestDto);

        ApiResponse<Long> response = ApiResponse.success(201, "상품 구매에 성공했습니다.", purchaseId);
        return ResponseEntity.ok(response);
    }
}