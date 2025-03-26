package com.example.carnation.domain.payment.impl.naver.helper;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.init.PropertyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class NaverPaymentHelper {
    public HttpHeaders getHeadersByNaverPayment() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id",PropertyInfo.PAYMENT_NAVER_CLIENT_ID);
        headers.set("X-Naver-Client-Secret",PropertyInfo.PAYMENT_NAVER_SECRET);
        headers.set("X-NaverPay-Chain-Id",PropertyInfo.PAYMENT_NAVER_CHAIN_ID);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public Map<String, String> getParamsByNaverPayment(Order entity) {
        Map<String, String> params = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        params.put("merchantPayKey", PropertyInfo.PAYMENT_NAVER_MERCHANT_ID);
        params.put("merchantUserKey", uuid + "_" +entity.getPayment().getPartnerUserId());
        params.put("productName", entity.getProduct().getProductName());
        params.put("productCount", String.valueOf(entity.getProduct().getQuantity()));
        params.put("totalPayAmount", String.valueOf(entity.getProduct().getPrice()));
        params.put("taxScopeAmount", String.valueOf(entity.getProduct().getPrice() * 100 / (100 + 10)));
        params.put("taxExScopeAmount", String.valueOf(entity.getPayment().getTaxFreeAmount()));
        params.put("returnUrl", PropertyInfo.SERVER_URL + "/api/v1/payment/naver/callback/approval?order_id=" + entity.getId());
        return params;
    }
}
