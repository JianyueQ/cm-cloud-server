package com.cm.controller.admin;

import com.cm.result.Result;
import com.cm.service.SystemService;
import com.cm.vo.ServiceMonitoringVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 31373
 */
@RestController
@RequestMapping("/admin/monitor")
public class SystemController {

    @Autowired
    private SystemService systemService;

    /**
     * 获取系统信息
     */
    @GetMapping("/server")
    public Result<ServiceMonitoringVO> getServer(){
        return Result.success(systemService.getServer());
    }


}
