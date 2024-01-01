package com.begcode.monolith.web.rest;

import static com.begcode.monolith.security.SecurityUtils.*;
import static com.begcode.monolith.security.SecurityUtils.AUTHORITIES_KEY;
import static com.begcode.monolith.security.SecurityUtils.JWT_ALGORITHM;

import cn.hutool.core.util.RandomUtil;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.config.Constants;
import com.begcode.monolith.domain.enumeration.ApiPermissionState;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.security.SecurityUtils;
import com.begcode.monolith.security.annotation.PermissionDefine;
import com.begcode.monolith.util.RandImageUtil;
import com.begcode.monolith.web.rest.errors.BadRequestAlertException;
import com.begcode.monolith.web.rest.vm.LoginVM;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.cache.CacheManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
public class AuthenticateController {

    private final Logger log = LoggerFactory.getLogger(AuthenticateController.class);
    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    protected final JwtEncoder jwtEncoder;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:0}")
    protected long tokenValidityInSeconds;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me:0}")
    protected long tokenValidityInSecondsForRememberMe;

    private final CacheManager cacheManager;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthenticateController(
        JwtEncoder jwtEncoder,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        CacheManager cacheManager
    ) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/authenticate")
    @PermissionDefine(
        groupName = "系统设置",
        groupCode = "GROUP_SYSTEM",
        entityName = "用户管理",
        entityCode = "USER",
        permissionName = "用户登录加验证码",
        permissionCode = "USER_AUTHENTICATE",
        state = ApiPermissionState.PERMIT_ALL
    )
    @AutoLog(value = "用户登录加验证码", logType = LogType.LOGIN, operateType = OperateType.LOGIN)
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        if (StringUtils.isNotBlank(loginVM.getCaptcha()) && StringUtils.isNotBlank(loginVM.getCaptcha())) {
            String lowerCaseCaptcha = loginVM.getCaptcha().toLowerCase();
            String realKey = DigestUtils.md5Hex(lowerCaseCaptcha + loginVM.getCheckKey());
            Object checkCode = cacheManager.getCache(Constants.CAPTCHA_KEY).get(realKey);
            if (checkCode == null || !checkCode.toString().equals(lowerCaseCaptcha)) {
                throw new BadRequestAlertException("验证码错误", "Login", "captchaError");
            }
        } else {
            throw new BadRequestAlertException("验证码错误", "Login", "captchaError");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.createTokenWithUserId(authentication, loginVM.isRememberMe(), SecurityUtils.getCurrentUserId().orElse(null));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/authenticate/withoutCaptcha")
    @PermissionDefine(
        groupName = "系统设置",
        groupCode = "GROUP_SYSTEM",
        entityName = "用户管理",
        entityCode = "USER",
        permissionName = "用户登录无验证码",
        permissionCode = "USER_AUTHENTICATE_WITHOUT_CAPTCHA",
        state = ApiPermissionState.PERMIT_ALL
    )
    @AutoLog(value = "用户登录无验证码", logType = LogType.LOGIN, operateType = OperateType.LOGIN)
    public ResponseEntity<JWTToken> authorizeWithoutCaptcha(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.createTokenWithUserId(authentication, loginVM.isRememberMe(), SecurityUtils.getCurrentUserId().orElse(null));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * {@code GET /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * 后台生成图形验证码 ：有效
     * @param key 前端提供key
     */
    @Operation(tags = "获取验证码")
    @GetMapping(value = "/randomImage/{key}")
    @PermissionDefine(
        groupName = "系统设置",
        groupCode = "GROUP_SYSTEM",
        entityName = "用户管理",
        entityCode = "USER",
        permissionName = "获取验证码",
        permissionCode = "USER_CAPTCHA",
        state = ApiPermissionState.PERMIT_ALL
    )
    public ResponseEntity<String> randomImage(@PathVariable String key) {
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = DigestUtils.md5Hex(lowerCaseCode + key);
            cacheManager.getCache(Constants.CAPTCHA_KEY).put(realKey, lowerCaseCode);
            String base64 = RandImageUtil.generate(code);
            return ResponseEntity.ok(base64);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadRequestAlertException("获取验证码出错", "Login", "randomImageError");
        }
    }

    /**
     * 登录二维码
     */
    @Operation(tags = "登录二维码")
    @GetMapping("/getLoginQrcode")
    public ResponseEntity<String> getLoginQrcode() {
        String qrcodeId = Constants.LOGIN_QRCODE_PRE + UUID.randomUUID();
        cacheManager.getCache(Constants.CAPTCHA_KEY).put(Constants.LOGIN_QRCODE + qrcodeId, qrcodeId);
        return ResponseEntity.ok(qrcodeId);
    }

    /**
     * 扫码二维码
     */
    @Operation(tags = "扫码登录二维码")
    @PostMapping("/scanLoginQrcode")
    @AutoLog(value = "扫码登录二维码", logType = LogType.LOGIN, operateType = OperateType.LOGIN)
    public ResponseEntity<String> scanLoginQrcode(@RequestParam String qrcodeId, @RequestParam String token) {
        Object check = cacheManager.getCache(Constants.CAPTCHA_KEY).get(Constants.LOGIN_QRCODE + qrcodeId);
        if (StringUtils.isNotBlank((String) check)) {
            //存放token给前台读取
            cacheManager.getCache(Constants.CAPTCHA_KEY).put(Constants.LOGIN_QRCODE_TOKEN + qrcodeId, token);
        } else {
            return ResponseEntity.badRequest().body("二维码已过期,请刷新后重试");
        }
        return ResponseEntity.ok("扫码成功");
    }

    /**
     * 获取用户扫码后保存的token
     */
    @Operation(tags = "获取用户扫码后保存的token")
    @GetMapping("/getQrcodeToken")
    public ResponseEntity<Map<String, Object>> getQrcodeToken(@RequestParam String qrcodeId) {
        Object token = cacheManager.getCache(Constants.CAPTCHA_KEY).get(Constants.LOGIN_QRCODE_TOKEN + qrcodeId);
        Map<String, Object> result = new HashMap<>(5);
        Object qrcodeIdExpire = cacheManager.getCache(Constants.CAPTCHA_KEY).get(Constants.LOGIN_QRCODE + qrcodeId);
        if (StringUtils.isBlank((String) qrcodeIdExpire)) {
            //二维码过期通知前台刷新
            result.put("token", "-2");
            return ResponseEntity.ok(result);
        }
        if (StringUtils.isNotBlank((String) token)) {
            result.put("success", true);
            result.put("token", token);
        } else {
            result.put("token", "-1");
        }
        return ResponseEntity.ok(result);
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);
        }

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String createTokenWithUserId(Authentication authentication, boolean rememberMe, Long id) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);
        }

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .claim(USER_ID, String.valueOf(id))
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    // add by wangxin start 增加session_key到jwt
    public String createTokenWithWx(Authentication authentication, boolean rememberMe, String sessionKey, Long id) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);
        }

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .claim(WX_SESSION_KEY,sessionKey)
            .claim(USER_ID, String.valueOf(id))
            .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    // add by wangxin end

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
