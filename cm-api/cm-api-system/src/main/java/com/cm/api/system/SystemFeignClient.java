package com.cm.api.system;


import com.cm.api.system.fallback.SystemFeignClientFallback;
import com.cm.entity.OperationLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户服务Feign客户端
 *
 * @author 31373
 */
@FeignClient(value = "cm-system", fallback = SystemFeignClientFallback.class)
public interface SystemFeignClient {

    /**
     * 保存操作日志
     */
    @PostMapping("/admin/operLog")
    public void insertOperLog(@RequestBody OperationLog operationLog) throws Exception;

}
