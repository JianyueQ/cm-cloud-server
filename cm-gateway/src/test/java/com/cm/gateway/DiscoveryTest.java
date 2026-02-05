package com.cm.gateway;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest
@Slf4j
public class DiscoveryTest {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private NacosServiceDiscovery nacosServiceDiscovery;

    @Test
    void nacosServiceDiscoveryTest() throws NacosException {
        for (String serviceName:nacosServiceDiscovery.getServices()){
            log.info("服务名：{}",serviceName);
            List<ServiceInstance> clientInstances = nacosServiceDiscovery.getInstances(serviceName);
            for (ServiceInstance serviceInstance:clientInstances){
                log.info("服务ip：{}，服务端口：{}",serviceInstance.getHost(),serviceInstance.getPort());
            }
        }
    }


    @Test
    void discoveryClientTest(){
        for (String serviceName:discoveryClient.getServices()){
            log.info("服务名：{}",serviceName);
            List<ServiceInstance> clientInstances = discoveryClient.getInstances(serviceName);
            for (ServiceInstance serviceInstance:clientInstances){
                log.info("服务ip：{}，服务端口：{}",serviceInstance.getHost(),serviceInstance.getPort());
            }
        }
    }

}
