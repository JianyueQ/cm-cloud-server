package com.cm.controller.common;

import com.cm.result.Result;
import com.cm.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author 31373
 */
@RequestMapping("/code")
@RestController
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 获取验证码
     * @return 验证码
     */
    @GetMapping("/get")
    public Result<Map<String,String>> getVerifyCode()
    {
        Map<String,String> verifyCode = verifyCodeService.generateVerifyCode();
        return Result.success(verifyCode);
    }

    /**
     * 刷新验证码
     * @param uuid uuid
     * @return 验证码
     * @throws IOException  io
     */
    @GetMapping("/refresh/{uuid}")
    public Result<String> refreshVerifyCode(@PathVariable String uuid) throws IOException {
        return Result.success(verifyCodeService.generateVerifyCodeByUuid(uuid));
    }

}
