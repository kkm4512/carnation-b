package com.example.carnation.config;

import com.example.carnation.common.exception.RestTemplateException;
import com.example.carnation.common.response.enums.ApiResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.INTERNAL_SERVER_ERROR;
import static com.example.carnation.common.response.enums.RestTemplateApiResponseEnum.*;

@Configuration
@Slf4j(topic = "RestTemplateConfig")
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(5000);
            requestFactory.setReadTimeout(5000);
            restTemplate.setRequestFactory(requestFactory);
            return restTemplate;
        } catch (Exception e) {
            throw new RestTemplateException(mapExceptionToResponse(e.getCause()));
        }
    }

    private ApiResponseEnum mapExceptionToResponse(Throwable cause) {
        return switch (cause.getClass().getSimpleName()) {
            case "SocketTimeoutException" -> REQUEST_TIMEOUT;
            case "ConnectTimeoutException" -> GATEWAY_TIMEOUT;
            case "UnknownHostException" -> SERVICE_UNAVAILABLE;
            case "SSLException" -> SSL_HANDSHAKE_FAILED;
            default -> INTERNAL_SERVER_ERROR;
        };
    }
}
