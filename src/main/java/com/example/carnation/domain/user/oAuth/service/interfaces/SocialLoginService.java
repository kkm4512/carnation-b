package com.example.carnation.domain.user.oAuth.service.interfaces;

import com.example.carnation.domain.user.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.user.common.entity.User;

public interface SocialLoginService {
    String getAccessToken(OAuthProviderDto dto);
    User getUser(OAuthProviderDto dto);
}
