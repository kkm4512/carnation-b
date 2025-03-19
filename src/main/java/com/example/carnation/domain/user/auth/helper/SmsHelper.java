package com.example.carnation.domain.user.auth.helper;

import com.example.carnation.common.exception.SmsException;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.INTERNAL_SERVER_ERROR;
import static com.example.carnation.common.response.enums.SmsApiResponseEnum.*;

@Component
public class SmsHelper {
    private DefaultMessageService messageService;

    @Value("${coolsms.api.key}")
    private String key;
    @Value("${coolsms.api.secret}")
    private String secretKey;
    @Value("${coolsms.api.url}")
    private String url;
    @Value("${coolsms.api.fromNumber}")
    private String fromNumber;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(
                key,
                secretKey,
                url
        );
    }


    public void sendSms(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(to); // 수신 번호
        message.setText("[Carnation] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        try {
            // 메시지 전송
            this.messageService.send(message);
        } catch (NurigoMessageNotReceivedException e) {
            throw new SmsException(SMS_SEND_FAILED,e);
        } catch (NurigoEmptyResponseException e) {
            throw new SmsException(EMPTY_RESPONSE, e);
        } catch (NurigoUnknownException e) {
            throw new SmsException(UNKNOWN_ERROR, e);
        } catch (Exception e) {
            throw new SmsException(INTERNAL_SERVER_ERROR, e);
        }
    }
}
