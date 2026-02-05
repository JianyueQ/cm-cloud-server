package com.cm.common.core.manager;




import com.cm.common.core.utils.SpringUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 *
 * @author 31373
 */
public class AsyncManager {

    /**
     * 操作延迟10毫秒
     */
    private final int OPERATE_DELAY_TIME = 10;

    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 单例模式
     */
    private AsyncManager(){}
    private static AsyncManager me = new AsyncManager();
    public static AsyncManager me()
    {
        return me;
    }

    /**
     * 执行任务
     * @param task 任务
     */
    public void execute(TimerTask task)
    {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行任务
     * @param task 任务
     * @param delay 延迟时间
     */
    public void execute(TimerTask task, long delay) {
        executor.schedule(task,delay,TimeUnit.MILLISECONDS);
    }
}
