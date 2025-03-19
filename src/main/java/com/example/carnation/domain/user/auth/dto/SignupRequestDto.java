package com.example.carnation.domain.user.auth.dto;

import com.example.carnation.domain.user.wallet.constans.BankType;
import com.example.carnation.security.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 회원가입 요청 DTO") // DTO 설명 추가
public class SignupRequestDto {

    @Schema(description = "사용자의 이메일 주소", example = "test@naver.com1")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Schema(
            description = "사용자의 비밀번호. 최소 8자 이상, 최대 20자 이하. " +
                    "대문자 1개 이상, 숫자 1개 이상, 특수문자 2개 이상 포함해야 함.",
            example = "!@Skdud3401"
    )
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&].*[@$!%*?&])(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 최소 하나의 대문자, 최소 두 개의 특수문자, 최소 하나의 숫자를 포함해야 합니다."
    )
    private String password;

    @Schema(
            description = "사용자의 닉네임. 3자 이상, 15자 이하. " +
                    "영문, 숫자, 한글만 허용됨.",
            example = "root123"
    )
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 3, max = 15, message = "닉네임은 3자 이상 15자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 영문, 숫자, 한글만 허용됩니다.")
    private String nickname;

    @Schema(description = "사용자의 휴대폰 번호 (숫자만 입력)", example = "01012345678")
    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(01[016789])\\d{3,4}\\d{4}$", message = "유효한 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @Schema(description = "사용자 역할 (ROLE_USER, ROLE_ADMIN 등)", example = "ROLE_USER", nullable = true, defaultValue = "ROLE_USER")
    private UserRole userRole;

    @Schema(description = "주민등록번호 (13자리)", example = "900101-2345678")
    @NotBlank(message = "주민등록번호는 필수 입력 항목입니다.") // 빈 값 불가
    @Pattern(
            regexp = "^(\\d{6})-(\\d{7})$",
            message = "주민등록번호 형식이 올바르지 않습니다. (예: 900101-2345678)"
    ) // 6자리-7자리 숫자 형식 검증
    private String residentRegistrationNumber;

    /** 사용자의 은행 */
    @Schema(description = "사용자의 은행", example = "KEB_HANA")
    @NotNull(message = "은행은 필수 입력 항목입니다.")  // ✅ Enum에는 @NotNull 사용!
    private BankType bank;

    /** 사용자의 계좌번호 */
    @Schema(description = "사용자의 계좌번호", example = "123-4567-8910")
    private String accountNumber;

}
