package com.cm.auth.controller.login;

import com.cm.auth.service.VerifyCodeService;
import com.cm.common.core.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * 验证码-控制器
 * @author 31373
 */
@RequestMapping("/code")
@RestController
public class VerifyCodeController {

    private final VerifyCodeService verifyCodeService;

    public VerifyCodeController(VerifyCodeService verifyCodeService) {
        this.verifyCodeService = verifyCodeService;
    }

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
