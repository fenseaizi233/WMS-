package com.fenseaizi.wmsdemo.controller;


import com.fenseaizi.wmsdemo.common.CaptureConfig;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping
@CrossOrigin
@RestController
public class CaptureController {
    @RequestMapping("/captcha")
    public void captcha(@RequestParam String key, HttpServletRequest request, HttpServletResponse respond) throws IOException {
        SpecCaptcha captcha = new SpecCaptcha(135,33,5);
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
       CaptureConfig.CAPTURE_MAP.put(key,captcha.text().toLowerCase());
        CaptchaUtil.out(captcha,request,respond);

        /*算术类型*/
        //ArithmeticCaptcha captcha = new ArithmeticCaptcha();
        //captcha.setLen(4);//几位数运算，默认是两位
        //captcha.getArithmeticString();//获取运算的公式：
        //captcha.text();
        //CaptchaUtil.out(captcha,request,respond);

    }
}
