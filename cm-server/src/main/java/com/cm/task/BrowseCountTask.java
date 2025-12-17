package com.cm.task;

import com.cm.constant.RedisConstant;
import com.cm.entity.Announcement;
import com.cm.mapper.AnnouncementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 31373
 */
@Slf4j
@Component
public class BrowseCountTask {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private AnnouncementMapper announcementMapper;

    /**
     * 定时任务：将Redis中的浏览计数同步到数据库
     * 每30分钟执行一次
     */
    @Scheduled(fixedRate = 30 * 60 * 1000)
//    @Scheduled(fixedRate = 5 * 1000) // 测试用,5秒更新
    public void syncReadCountToDatabase() {
        log.info("开始同步Redis中的浏览计数到数据库");
        String redisKey = RedisConstant.ANNOUNCEMENT_READ_COUNT;
        // 获取Redis中所有的公告阅读计数
        Map<Object, Object> redisReadCounts = redisTemplate.opsForHash().entries(redisKey);

        if (!redisReadCounts.isEmpty()) {
            for (Map.Entry<Object, Object> entry : redisReadCounts.entrySet()) {
                try {
                    Long announcementId = Long.valueOf(entry.getKey().toString());
                    Integer redisCount = Integer.valueOf(entry.getValue().toString());

                    // 获取数据库中的阅读计数
                    Announcement announcement = announcementMapper.getAnnouncementDetails(announcementId);
                    if (announcement != null) {
                        Integer dbCount = announcement.getReadCount() != null ? announcement.getReadCount() : 0;

                        // 如果Redis中的计数大于数据库中的计数，则更新数据库
                        if (redisCount > dbCount) {
                            Announcement updateAnnouncement = new Announcement();
                            updateAnnouncement.setId(announcementId);
                            updateAnnouncement.setReadCount(redisCount);
                            announcementMapper.updateAnnouncement(updateAnnouncement);
                        }
                    }
                } catch (Exception e) {
                    // 记录异常但继续处理其他公告
                    e.printStackTrace();
                }
            }
        }
    }
}
