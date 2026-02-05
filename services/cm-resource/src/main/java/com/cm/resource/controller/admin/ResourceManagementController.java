package com.cm.resource.controller.admin;

import com.cm.common.aop.annotations.Log;
import com.cm.common.core.result.Result;
import com.cm.common.enumeration.BusinessType;
import com.cm.dto.ResourceDTO;
import com.cm.dto.ResourceListDTO;
import com.cm.resource.service.ResourceManagementService;
import com.cm.vo.ResourceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 31373
 */
@Slf4j
@RestController("adminResourceManagementController")
@RequestMapping("/admin/resource")
public class ResourceManagementController {

    private final ResourceManagementService resourceManagementService;

    public ResourceManagementController(ResourceManagementService resourceManagementService) {
        this.resourceManagementService = resourceManagementService;
    }

    /**
     * 添加文件夹/上传文件
     */
    @PostMapping("/add")
    @Log(title = "管理端-添加文件夹/上传文件", businessType = BusinessType.INSERT)
    public Result<String> addResource(@RequestBody ResourceDTO resourceDTO) {
        log.info("添加文件夹/上传文件: {}", resourceDTO);
        resourceManagementService.addResource(resourceDTO);
        return Result.success();
    }

    /**
     * 获取文件夹列表/获取文件列表
     */
    @GetMapping("/list")
    public Result<List<ResourceVO>> getResourceList(ResourceListDTO resourceListDTO) {
        log.info("获取文件夹列表/获取文件列表: {}", resourceListDTO);
        List<ResourceVO> resourceVOList = resourceManagementService.getResourceList(resourceListDTO);
        return Result.success(resourceVOList);
    }

    /**
     * 修改文件夹名称
     */
    @PutMapping("/update")
    @Log(title = "管理端-修改文件夹名称", businessType = BusinessType.UPDATE)
    public Result<String> updateResource(@RequestBody ResourceDTO resourceDTO) {
        log.info("修改文件夹名称: {}", resourceDTO);
        resourceManagementService.updateResource(resourceDTO);
        return Result.success();
    }

    /**
     * 删除文件夹/删除文件
     */
    @DeleteMapping("/delete")
    @Log(title = "管理端-删除文件夹/删除文件", businessType = BusinessType.DELETE)
    public Result<String> deleteResource(@RequestParam("id") Long id, @RequestParam("fileUrls") String fileUrls) {
        log.info("删除文件夹/删除文件: {}", id);
        resourceManagementService.deleteResource(id, fileUrls);
        return Result.success();
    }
}
