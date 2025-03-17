package com.example.carnation.domain.oAuth.service.interfaces;

import com.example.carnation.domain.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.user.entity.User;

public interface SocialLoginService {
    String getAccessToken(OAuthProviderDto dto);
    User getUser(OAuthProviderDto dto);
}
