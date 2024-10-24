package com.begcode.monolith.web.rest.safety;

import com.begcode.captcha.model.common.ResponseModel;
import com.begcode.captcha.model.vo.CaptchaVO;
import com.begcode.monolith.service.safety.SafetyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/safety")
@Tag(name = "safetyVerify", description = "安全验证控制器")
public class SafetyVerifyResource {

    private final SafetyService safetyService;

    public SafetyVerifyResource(SafetyService safetyService) {
        this.safetyService = safetyService;
    }

    @Operation(tags = "获取行为验证码", description = "获取行为验证码")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> getSafetyCode(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return ResponseEntity.ok(safetyService.getSafetyCode(data, request));
    }

    @Operation(tags = "验证行为验证码", description = "验证行为验证码")
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> checkSafetyCode(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return ResponseEntity.ok(safetyService.checkSafetyCode(data, request));
    }

    @Operation(tags = "行为验证码二次校验", description = "行为验证码二次校验")
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> verifySafetyCode(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return ResponseEntity.ok(safetyService.verifySafetyCode(data));
    }
}
