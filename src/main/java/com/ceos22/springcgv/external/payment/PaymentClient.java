package com.ceos22.springcgv.external.payment;

import com.ceos22.springcgv.dto.payment.PaymentRequest;
import com.ceos22.springcgv.dto.payment.PaymentResponse;
import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
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

    public PaymentResponse requestPayment(String paymentId, PaymentRequest request) {
        String url = paymentBaseUrl + "/payments/" + paymentId + "/instant";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(paymentApiSecret);

        HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, PaymentResponse.class);

            return response.getBody();

        } catch (Exception e) {
            log.error("결제 요청 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.PAYMENT_FAILED);
        }
    }
}
