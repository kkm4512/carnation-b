package com.example.carnation.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyInfo {

    // 🔐 Social.Kakao
    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    // 🔐 Social.Naver
    @Value("${social.naver.client-id}")
    private String naverClientId;

    @Value("${social.naver.client-secret}")
    private String naverClientSecret;

    // 🔐 Social.Google
    @Value("${social.google.client-id}")
    private String googleClientId;

    @Value("${social.google.client-secret}")
    private String googleClientSecret;

    // 🔐 Payment.Kakao
    @Value("${payment.kakao.client-id}")
    private String paymentKakaoClientId;

    @Value("${payment.kakao.secret}")
    private String paymentKakaoSecret;

    @Value("${payment.kakao.secret-key}")
    private String paymentKakaoSecretKey;

    @Value("${payment.kakao.secret-key-dev}")
    private String paymentKakaoSecretKeyDev;

    @Value("${payment.kakao.merchant-id}")
    private String paymentKakaoMerchantId;

    // 🔐 Payment.Naver
    @Value("${payment.naver.client-id}")
    private String paymentNaverClientId;

    @Value("${payment.naver.secret}")
    private String paymentNaverSecret;

    @Value("${payment.naver.chain-id}")
    private String paymentNaverChainId;

    @Value("${payment.naver.merchant-id}")
    private String paymentNaverMerchantId;

    // 🔐 Cloud.Aws.S3
    @Value("${cloud.aws.s3.bucket}")
    private String awsS3Bucket;

    // 🔐 Cloud.Aws.Stack
    @Value("${cloud.aws.stack.auto}")
    private boolean awsStackAuto;

    // 🔐 Cloud.Aws.Region
    @Value("${cloud.aws.region.static}")
    private String awsRegionStatic;

    // 🔐 Cloud.Aws.Credentials
    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    // 🔐 Server
    @Value("${server.url}")
    private String serverUrl;

    @Value("${social.redirect.url}")
    private String redirectUrl;

    // === Static Fields ===
    public static String KAKAO_CLIENT_ID;
    public static String NAVER_CLIENT_ID;
    public static String NAVER_CLIENT_SECRET;
    public static String GOOGLE_CLIENT_ID;
    public static String GOOGLE_CLIENT_SECRET;

    public static String PAYMENT_KAKAO_CLIENT_ID;
    public static String PAYMENT_KAKAO_SECRET;
    public static String PAYMENT_KAKAO_SECRET_KEY;
    public static String PAYMENT_KAKAO_SECRET_KEY_DEV;
    public static String PAYMENT_KAKAO_MERCHANT_ID;

    public static String PAYMENT_NAVER_CLIENT_ID;
    public static String PAYMENT_NAVER_SECRET;
    public static String PAYMENT_NAVER_CHAIN_ID;
    public static String PAYMENT_NAVER_MERCHANT_ID;

    public static String AWS_S3_BUCKET;
    public static boolean AWS_STACK_AUTO;
    public static String AWS_REGION_STATIC;
    public static String AWS_ACCESS_KEY;
    public static String AWS_SECRET_KEY;

    public static String SERVER_URL;
    public static String SOCIAL_REDIRECT_URL;

    // === 초기화 ===
    @PostConstruct
    public void init() {
        KAKAO_CLIENT_ID = kakaoClientId;
        NAVER_CLIENT_ID = naverClientId;
        NAVER_CLIENT_SECRET = naverClientSecret;
        GOOGLE_CLIENT_ID = googleClientId;
        GOOGLE_CLIENT_SECRET = googleClientSecret;

        PAYMENT_KAKAO_CLIENT_ID = paymentKakaoClientId;
        PAYMENT_KAKAO_SECRET = paymentKakaoSecret;
        PAYMENT_KAKAO_SECRET_KEY = paymentKakaoSecretKey;
        PAYMENT_KAKAO_SECRET_KEY_DEV = paymentKakaoSecretKeyDev;
        PAYMENT_KAKAO_MERCHANT_ID = paymentKakaoMerchantId;

        PAYMENT_NAVER_CLIENT_ID = paymentNaverClientId;
        PAYMENT_NAVER_SECRET = paymentNaverSecret;
        PAYMENT_NAVER_CHAIN_ID = paymentNaverChainId;
        PAYMENT_NAVER_MERCHANT_ID = paymentNaverMerchantId;

        AWS_S3_BUCKET = awsS3Bucket;
        AWS_STACK_AUTO = awsStackAuto;
        AWS_REGION_STATIC = awsRegionStatic;
        AWS_ACCESS_KEY = awsAccessKey;
        AWS_SECRET_KEY = awsSecretKey;

        SERVER_URL = serverUrl;
        SOCIAL_REDIRECT_URL = redirectUrl;
    }
}
