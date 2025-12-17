package com.cm.controller.teacher;

import com.cm.dto.AnnouncementPageDTO;
import com.cm.result.PageResult;
import com.cm.result.Result;
import com.cm.service.AnnouncementService;
import com.cm.vo.AnnouncementDetailsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 31373
 */
@Slf4j
@RestController("teacherAnnouncementController")
@RequestMapping("/teacher/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 查询公告详情
     */
    @GetMapping("/{id}")
    public Result<AnnouncementDetailsVO> get(@PathVariable Long id) {
        log.info("查询公告详情:{}", id);
        AnnouncementDetailsVO announcementDetailsVO = announcementService.getAnnouncementDetails(id);
        return Result.success(announcementDetailsVO);
    }

    /**
     * 获取公告列表
     */
    @GetMapping("/list")
    public Result<PageResult> list(AnnouncementPageDTO announcementPageDTO) {
        log.info("查询公告列表");
        PageResult pageResult = announcementService.list(announcementPageDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取公告浏览数量
     */
    @GetMapping("/browseCount/{id}")
    public Result<Integer> browseCount(@PathVariable Long id) {
        log.info("获取公告浏览数量");
        return Result.success(announcementService.browseCount(id));
    }
}
