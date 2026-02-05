package com.cm.api.system.fallback;

import com.cm.api.system.SystemFeignClient;

import com.cm.entity.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 系统服务Feign客户端备用方案
 *
 * @author 31373
 */
@Component
public class SystemFeignClientFallback implements SystemFeignClient {

    private static final Logger log = LoggerFactory.getLogger(SystemFeignClientFallback.class);


    @Override
    public void insertOperLog(OperationLog operationLog) throws Exception {
        log.info("保存操作日志失败:{}", operationLog);
    }
}
