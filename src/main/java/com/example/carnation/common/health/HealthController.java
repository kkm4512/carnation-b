package com.example.carnation.common.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health API", description = "헬스 체크 API")
public class HealthController {

    @Operation(
            summary = "헬스 체크",
            description = "서버상태 체크 컨트롤러"
    )
    @GetMapping
    public String health() {
        return "OK";
    }
}
