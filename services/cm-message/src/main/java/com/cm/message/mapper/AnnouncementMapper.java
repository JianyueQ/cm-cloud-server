package com.cm.message.mapper;

import com.cm.common.aop.annotations.AutoFile;
import com.cm.common.enumeration.OperationType;
import com.cm.dto.AnnouncementPageDTO;
import com.cm.entity.Announcement;
import com.cm.vo.AnnouncementPageVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface AnnouncementMapper {
    /**
     * 添加公告
     *
     * @param announcement 添加的数据
     */
    @AutoFile(OperationType.INSERT)
    void addAnnouncement(Announcement announcement);

    /**
     * 修改公告
     * @param announcement 修改的数据
     */
    @AutoFile(OperationType.UPDATE)
    void updateAnnouncement(Announcement announcement);

    /**
     * 删除公告
     * @param ids 删除的id
     */
    void deleteAnnouncement(List<Long> ids);

    /**
     * 获取公告详情
     * @param id 详情的id
     * @return 详情
     */
    Announcement getAnnouncementDetails(Long id);

    /**
     * 获取公告列表
     * @param announcementPageDTO 查询条件
     * @return 列表
     */
    Page<AnnouncementPageVO> page(AnnouncementPageDTO announcementPageDTO);

    /**
     * 浏览次数
     * @param id 浏览的id
     * @return 浏览次数
     */
    Announcement getAnnouncementWithReadCount(Long id);
}
