package com.cm.service;

import com.cm.vo.ServiceMonitoringVO;

/**
 * @author 31373
 */
public interface SystemService {
    /**
     * 获取服务监控信息
     * @return 服务监控信息
     */
    ServiceMonitoringVO getServer();

}
