package com.example.carnation.aop;

import com.example.carnation.common.service.RedisService;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j(topic = "AspectModule")
@Component
@RequiredArgsConstructor
public class AspectModule {
    private final RedisService redisService;

    @Pointcut("@annotation(com.example.carnation.annotation.SaveRefreshToken)")
    public void saveRefreshTokenPointcut() {}

    @Around("saveRefreshTokenPointcut()")
    public Object saveRefreshToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(); // 원래 메서드 실행

        if (result instanceof TokenDto) {
            TokenDto tokenDto = (TokenDto) result;
            Long userId = tokenDto.getUserId();
            String refreshToken = tokenDto.getRefreshToken();
            redisService.saveRefreshToken(userId, refreshToken);
        }
        return result;
    }


}
