package com.cm.config;

import com.cm.utils.Threads;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 31373
 */
@Configuration
public class ThreadPoolConfig {

    // 核心线程池大小
    private int corePoolSize = 50;

    /**
     * 执行周期性任务的线程池
     */
    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                //线程池对拒绝任务（无线程可用）的处理策略
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()){
            //运行结束之后,线程数大于核心数，且当前线程空闲时间超过keepAliveTime，则销毁此线程
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                //线程执行异常处理,打印线程异常信息
                Threads.printException(r, t);
            }
        };

    }


}
