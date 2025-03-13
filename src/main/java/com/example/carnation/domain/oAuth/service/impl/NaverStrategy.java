package com.example.carnation.domain.oAuth.service.impl;

import com.example.carnation.common.exception.RestTemplateException;
import com.example.carnation.common.response.enums.RestTemplateApiResponse;
import com.example.carnation.domain.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.oAuth.dto.OAuthUserDto;
import com.example.carnation.domain.oAuth.service.interfaces.SocialLoginStrategy;
import com.example.carnation.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.example.carnation.common.response.enums.RestTemplateApiResponse.FAILED_TO_FETCH_SOCIAL_ACCESS_TOKEN;

@Slf4j(topic = "NaverStrategy")
@RequiredArgsConstructor
public class NaverStrategy implements SocialLoginStrategy {
    private final OAuthProviderDto oAuthProviderDto;
    private final String clientId;
    private final String clientSecret;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String getAccessToken(OAuthProviderDto dto) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", dto.getRedirectUrl());
        params.add("code", dto.getCode());
        params.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Map<String, Object> response = restTemplate.exchange(
                oAuthProviderDto.getTokenUrl(),
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        ).getBody();

        if (response == null || !response.containsKey("access_token")) {
            throw new RestTemplateException(FAILED_TO_FETCH_SOCIAL_ACCESS_TOKEN);
        }
        return (String) response.get("access_token");
    }

    @Override
    public User getUser(OAuthProviderDto dto) {
        try {
            String url = oAuthProviderDto.getUserInfoUrl();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(oAuthProviderDto.getAccessToken());

            HttpEntity<String> request = new HttpEntity<>(headers);
            String responseBody = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();

            if (responseBody == null) {
                throw new RestTemplateException(RestTemplateApiResponse.FAILED_TO_FETCH_SOCIAL_USER_INFO);
            }

            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode responseNode = jsonNode.get("response");
            String email = responseNode.get("email").asText();
            String nickname = responseNode.get("nickname").asText();
            OAuthUserDto oAuthUserDto = OAuthUserDto.of(email, nickname);
            return User.of(oAuthUserDto);
        } catch (HttpClientErrorException e) {
            log.error("OAuth API 호출 실패: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponse.FAILED_TO_FETCH_SOCIAL_USER_INFO,e);
        } catch (HttpServerErrorException e) {
            log.error("OAuth 제공자 서버 오류 발생: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponse.OAUTH_PROVIDER_SERVER_ERROR,e);
        } catch (JsonProcessingException e) {
            log.error("OAuth 응답 JSON 파싱 실패: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponse.INVALID_SOCIAL_RESPONSE,e);
        } catch (IllegalArgumentException e) {
            log.error("OAuth 응답 데이터 형식 오류: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponse.INVALID_SOCIAL_USER_INFO,e);
        } catch (RestTemplateException e) {
            log.error("RestTemplate 요청 오류: {}", e.getMessage());
            throw e; // 이미 정의된 RestTemplateException 그대로 던지기
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponse.UNEXPECTED_ERROR,e);
        }
    }
}
