package com.example.carnation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Refresh Token을 Redis에 저장하는 커스텀 어노테이션
 */
@Target(ElementType.METHOD)  // 메서드에만 적용 가능
@Retention(RetentionPolicy.RUNTIME)  // 런타임 동안 유지
public @interface SaveRefreshToken {
}
