package com.cm.system.controller.admin;

import com.cm.common.core.result.Result;
import com.cm.system.service.SystemService;
import com.cm.vo.ServiceMonitoringVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 31373
 */
@RestController
@RequestMapping("/admin/monitor")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    /**
     * 获取系统信息
     */
    @GetMapping("/server")
    public Result<ServiceMonitoringVO> getServer(){
        return Result.success(systemService.getServer());
    }


}
