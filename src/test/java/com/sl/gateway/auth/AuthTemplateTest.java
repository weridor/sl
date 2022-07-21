package com.sl.gateway.auth;

import cn.hutool.json.JSONUtil;
import com.itheima.auth.boot.autoconfigure.AuthorityProperties;
import com.itheima.auth.sdk.AuthTemplate;
import com.itheima.auth.sdk.common.Result;
import com.itheima.auth.sdk.dto.AuthUserInfoDTO;
import com.itheima.auth.sdk.dto.LoginDTO;
import com.itheima.auth.sdk.service.TokenCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zzj
 * @version 1.0
 */
@SpringBootTest(properties = "spring.main.web-application-type = reactive")
public class AuthTemplateTest {

    @Resource
    private AuthTemplate authTemplate;
    @Resource
    private TokenCheckService tokenCheckService;
    @Autowired
    private AuthorityProperties authorityProperties;

    @Test
    public void testLogin() {
        Result<LoginDTO> result = this.authTemplate.opsForLogin()
                .token("courier", "123456");
        System.out.println(result.getData().getToken().getToken());

        authTemplate.getAuthorityConfig().setToken(result.getData().getToken().getToken());
        Long id = result.getData().getUser().getId();
        System.out.println(result.getData().getUser());
        System.out.println(authTemplate.opsForRole().findRoleByUserId(id));
    }

    @Test
    public void checkToken() {
        //admin
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5ODQxMjUyNTk4MzE1ODU4ODkiLCJhY2NvdW50IjoiY291cmllciIsIm5hbWUiOiLlv6vpgJLlkZgiLCJvcmdpZCI6OTg3MzI2OTY5MzYyMjg4MTkzLCJzdGF0aW9uaWQiOjk4MTIyMzcwMzMzNTQxMDYyNSwiYWRtaW5pc3RyYXRvciI6ZmFsc2UsImV4cCI6MTY1NTg0MDQ0Nn0.eX0dinc72s1QsXqOFxX67XU_qrCyhXvFJvVuruKyrojTAtvGOSemFfhATmGOCfdSd_A_uStMlB6AzbCJZJKM8w";
        // String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5ODQxMjUyNTk4MzE1ODU4ODkiLCJhY2NvdW50IjoiY291cmllciIsIm5hbWUiOiLlv6vpgJLlkZgiLCJvcmdpZCI6OTc3Mjc0OTMzMDIzMzQ0MDMzLCJzdGF0aW9uaWQiOjk4MTIyMzcwMzMzNTQxMDYyNSwiYWRtaW5pc3RyYXRvciI6ZmFsc2UsImV4cCI6MTY1NTE1NjY1NX0.jNLni8UYqQ0ZxHLNFVz4sTh_emrCSMKTQyltc77B5z7sHEEAsC0qUgdWUARmnQyp0NskeUKQtWpSgWZ_T1ULKQ";
        AuthUserInfoDTO authUserInfo = this.tokenCheckService.parserToken(token);
        System.out.println(authUserInfo);

        System.out.println(JSONUtil.toJsonStr(authUserInfo));

    }
}
