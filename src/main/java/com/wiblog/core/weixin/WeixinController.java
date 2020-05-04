package com.wiblog.core.weixin;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/7/25
 */
@Controller
@Slf4j
public class WeixinController {

    @Autowired
    private WeixinUtil weixinUtil;

    /**
     * 接收微信返回消息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/tokenCheck", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void tokenCheck(HttpServletRequest request, HttpServletResponse response) {
        log.info("接收微信请求");
        try (PrintWriter print = response.getWriter()){
            //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
            request.setCharacterEncoding("UTF-8");
            //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
            response.setCharacterEncoding("UTF-8");
            boolean isGet = "get".equals(request.getMethod().toLowerCase());
            if (isGet) {
                // 微信加密签名
                String signature = request.getParameter("signature");
                // 时间戳
                String timestamp = request.getParameter("timestamp");
                // 随机数
                String nonce = request.getParameter("nonce");
                // 随机字符串
                String echostr = request.getParameter("echostr");
                if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
                    print.write(echostr);
                    print.flush();
                }
            }else {
                String respMessage;

                //进入post处理
                respMessage = weixinUtil.messageHandler(request);
                if (print == null || StringUtils.isBlank(respMessage)){
                    return;
                }
                print.write(respMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/weixin/getAccessToken")
    @ResponseBody
    public String getAccessToken() {
        return weixinUtil.getAccessToken();
    }

    @GetMapping("/weixin/sign")
    @ResponseBody
    public Map<String, String> sign(String url) {
        return weixinUtil.sign(url);
    }

    @GetMapping("/weixin/test")
    public String weixin() {
        return "test";
    }


}
