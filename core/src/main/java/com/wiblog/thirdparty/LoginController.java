package com.wiblog.thirdparty;

import com.wiblog.common.ServerResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/11/1
 */
@RestController
public class LoginController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/github/callback")
    public ServerResponse githubLogin(String code, String state, String client_id, HttpServletRequest request){
        String token = githubProvider.getAccessToken(code);
        return ServerResponse.success(token);
    }
}
