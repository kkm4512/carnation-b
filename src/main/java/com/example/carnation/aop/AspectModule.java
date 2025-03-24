package com.example.carnation.aop;

import com.example.carnation.common.service.RedisService;
import com.example.carnation.domain.user.auth.dto.PhoneAuthRequestDto;
import com.example.carnation.domain.user.auth.dto.PhoneAuthResponseDto;
import com.example.carnation.domain.user.auth.dto.TokenRefreshRequestDto;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.example.carnation.security.JwtManager.BEARER_PREFIX;

@Aspect
@Slf4j(topic = "AspectModule")
@Component
@RequiredArgsConstructor
public class AspectModule {
    private final RedisService redisService;

    @Pointcut("@annotation(com.example.carnation.annotation.RotateToken)")
    public void RotateTokenPointcut() {}

    @Around("RotateTokenPointcut()")
    public Object rotateToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(); // 원래 메서드 실행

        // TokenDto 저장 로직도 유지
        Long userId = null;
        if (result instanceof TokenDto tokenDto) {
            userId = tokenDto.getUserId();
            String refreshToken = tokenDto.getRefreshToken();
            redisService.saveRefreshToken(userId, refreshToken);
        }

        // 메서드 파라미터 확인
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof TokenRefreshRequestDto dto) {
                String oldAccessToken = dto.getAccessToken();
                oldAccessToken = oldAccessToken.replace(BEARER_PREFIX, "");
                // 예: 블랙리스트 저장
                redisService.saveAccessTokenBlacklist(userId, oldAccessToken);
            }
        }

        return result;
    }

    @Pointcut("@annotation(com.example.carnation.annotation.SaveRefreshToken)")
    public void SaveRefreshTokenPointcut() {}

    @Around("SaveRefreshTokenPointcut()")
    public Object saveRefreshToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(); // 원래 메서드 실행

        // TokenDto 저장 로직도 유지
        if (result instanceof TokenDto tokenDto) {
            Long userId = tokenDto.getUserId();
            String refreshToken = tokenDto.getRefreshToken();
            redisService.saveRefreshToken(userId, refreshToken);
        }

        return result;
    }



    @Pointcut("@annotation(com.example.carnation.annotation.SaveVerificationCode)")
    public void saveVerificationCodePointcut() {}

    @Around("saveVerificationCodePointcut()")
    public Object saveVerificationCode(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof PhoneAuthResponseDto) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof PhoneAuthRequestDto) {
                redisService.saveVerificationCode(((PhoneAuthResponseDto) result).getPhoneNumber(), ((PhoneAuthResponseDto) result).getVerificationCode());
            }
        }
        return result;
    }

}
