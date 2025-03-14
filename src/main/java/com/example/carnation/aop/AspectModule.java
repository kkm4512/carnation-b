package com.example.carnation.aop;

import com.example.carnation.common.service.RedisService;
import com.example.carnation.domain.auth.dto.PhoneAuthResponseDto;
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

    @Pointcut("@annotation(com.example.carnation.annotation.SaveVerificationCode)")
    public void saveVerificationCodePointcut() {}

    @Around("saveVerificationCodePointcut()")
    public Object saveVerificationCode(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof PhoneAuthResponseDto) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof com.example.carnation.domain.auth.dto.PhoneAuthRequestDto) {
                redisService.saveVerificationCode(((PhoneAuthResponseDto) result).getPhoneNumber(), ((PhoneAuthResponseDto) result).getVerificationCode());
            }
        }
        return result;
    }

}
