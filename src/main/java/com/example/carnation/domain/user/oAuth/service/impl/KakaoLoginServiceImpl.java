package com.example.carnation.domain.user.oAuth.service.impl;

import com.example.carnation.common.exception.RestTemplateException;
import com.example.carnation.common.response.enums.RestTemplateApiResponseEnum;
import com.example.carnation.domain.user.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.user.oAuth.dto.OAuthUserDto;
import com.example.carnation.domain.user.oAuth.service.interfaces.SocialLoginService;
import com.example.carnation.domain.user.common.constans.AuthProvider;
import com.example.carnation.domain.user.common.entity.User;
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

import static com.example.carnation.common.response.enums.RestTemplateApiResponseEnum.FAILED_TO_FETCH_SOCIAL_ACCESS_TOKEN;

@Slf4j(topic = "KakaoStrategy")
@RequiredArgsConstructor
public class KakaoLoginServiceImpl implements SocialLoginService {
    private final OAuthProviderDto providerDto;
    private final String clientId;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public String getAccessToken(OAuthProviderDto dto) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", dto.getRedirectUrl());
        params.add("code", dto.getCode());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Map<String, Object> response = restTemplate.exchange(
                providerDto.getTokenUrl(),
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
    public User getUser(OAuthProviderDto oAuthProviderDto) {
        try {
            String url = oAuthProviderDto.getUserInfoUrl();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(oAuthProviderDto.getAccessToken());

            HttpEntity<String> request = new HttpEntity<>(headers);
            String responseBody = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();

            if (responseBody == null) {
                throw new RestTemplateException(RestTemplateApiResponseEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO);
            }

            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String email = jsonNode.get("kakao_account").get("email").asText();
            String nickname = jsonNode.get("properties").get("nickname").asText();
            OAuthUserDto oAuthUserDto = OAuthUserDto.of(email, nickname);
            return User.of(oAuthUserDto, AuthProvider.KAKAO);
        } catch (HttpClientErrorException e) {
            log.error("OAuth API 호출 실패: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponseEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO);
        } catch (HttpServerErrorException e) {
            log.error("OAuth 제공자 서버 오류 발생: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponseEnum.OAUTH_PROVIDER_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            log.error("OAuth 응답 JSON 파싱 실패: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponseEnum.INVALID_SOCIAL_RESPONSE);
        } catch (IllegalArgumentException e) {
            log.error("OAuth 응답 데이터 형식 오류: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponseEnum.INVALID_SOCIAL_USER_INFO);
        } catch (RestTemplateException e) {
            log.error("RestTemplate 요청 오류: {}", e.getMessage());
            throw e; // 이미 정의된 RestTemplateException 그대로 던지기
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw new RestTemplateException(RestTemplateApiResponseEnum.UNEXPECTED_ERROR);
        }
    }


}