package com.ceos22.springcgv.external.payment;

import com.ceos22.springcgv.dto.payment.PaymentCancelResponseDto;
import com.ceos22.springcgv.dto.payment.PaymentRequestDto;
import com.ceos22.springcgv.dto.payment.PaymentResponseDto;
import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${payment.base-url}")
    private String paymentBaseUrl;

    @Value("${payment.api-secret}")
    private String paymentApiSecret;

    public PaymentResponseDto requestPayment(String paymentId, PaymentRequestDto request) {
        String url = paymentBaseUrl + "/payments/" + paymentId + "/instant";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(paymentApiSecret);

        HttpEntity<PaymentRequestDto> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<PaymentResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, PaymentResponseDto.class);

            return response.getBody();

        } catch (Exception e) {
            log.error("결제 요청 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.PAYMENT_FAILED);
        }
    }

    public PaymentCancelResponseDto cancelPayment(String paymentId) {
        String url = paymentBaseUrl + "/payments/" + paymentId + "/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paymentApiSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<PaymentCancelResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, PaymentCancelResponseDto.class);
            return response.getBody();

        } catch (HttpStatusCodeException e) {
            log.error("[결제 취소 실패] status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new CustomException(ErrorCode.PAYMENT_CANCEL_FAILED);
        } catch (Exception e) {
            log.error("[결제 취소 요청 중 예외 발생] {}", e.getMessage());
            throw new CustomException(ErrorCode.PAYMENT_CANCEL_FAILED);
        }
    }
}
