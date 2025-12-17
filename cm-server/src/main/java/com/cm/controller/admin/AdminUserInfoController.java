package com.cm.controller.admin;

import com.cm.annotations.Log;
import com.cm.constant.SuccessConstant;
import com.cm.context.BaseContext;
import com.cm.dto.*;
import com.cm.enumeration.BusinessType;
import com.cm.result.PageResult;
import com.cm.result.Result;
import com.cm.service.AdminUserInfoService;
import com.cm.vo.AdminUserInfoVO;
import com.cm.vo.AdminWithUserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 31373
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminUserInfoController {

    @Autowired
    private AdminUserInfoService adminUserInfoService;

    /**
     * 获取用户信息
     */
    @GetMapping("/userInfo")
    public Result<AdminUserInfoVO> getUserInfo() {
        return Result.success(adminUserInfoService.getUserInfo());
    }

    /**
     * 更新用户信息
     */
    @Log(title = "用户信息-基本资料", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public Result<String> updateUserInfo(@RequestBody AdminUserInfoUpdateDTO adminUserInfoUpdateDTO) {
        log.info("更新用户信息:{}",adminUserInfoUpdateDTO);
        adminUserInfoService.updateUserInfo(adminUserInfoUpdateDTO);
        return Result.success(SuccessConstant.UPDATE_SUCCESS);
    }

    /**
     * 修改密码
     */
    @Log(title = "用户信息-修改密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public Result<String> updatePassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        log.info("用户:{}:修改密码:{}", BaseContext.getCurrentId(), resetPasswordDTO);
        adminUserInfoService.updatePassword(resetPasswordDTO);
        return Result.success(SuccessConstant.UPDATE_SUCCESS);
    }

    /**
     * 添加管理员用户信息
     */
    @Log(title = "管理员用户信息-添加", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody AdminUserInfoDTO adminUserInfoDTO) {
        log.info("添加管理员用户:{}",adminUserInfoDTO);
        adminUserInfoService.addUser(adminUserInfoDTO);
        return Result.success(SuccessConstant.ADD_SUCCESS);
    }

    /**
     * 修改管理员用户信息
     */
    @Log(title = "管理员用户信息-修改", businessType = BusinessType.UPDATE)
    @PutMapping("/updateUserInfo")
    public Result<String> updateUser(@RequestBody AdminUserInfoDTO adminUserInfoDTO) {
        log.info("修改管理员用户:{}",adminUserInfoDTO);
        adminUserInfoService.updateUser(adminUserInfoDTO);
        return Result.success(SuccessConstant.UPDATE_SUCCESS);
    }

    /**
     * 删除管理员用户信息
     */
    @Log(title = "管理员用户信息-删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete")
    public Result<String> deleteUser(@RequestParam List<Long> ids) {
        log.info("删除管理员用户:{}",ids);
        adminUserInfoService.deleteUser(ids);
        return Result.success(SuccessConstant.DELETE_SUCCESS);
    }

    /**
     * 获取管理员用户信息分页查询
     */
    @GetMapping("/list")
    public Result<PageResult> list(AdminUserInfoPageDTO adminUserInfoPageDTO) {
        log.info("获取管理员用户信息分页查询:{}", adminUserInfoPageDTO);
        return Result.success(adminUserInfoService.list(adminUserInfoPageDTO));
    }

    /**
     * 获取管理员用户信息详情
     */
    @GetMapping("/{id}")
    public Result<AdminWithUserInfoVO> get(@PathVariable Long id) {
        log.info("获取管理员用户信息详情:{}", id);
        return Result.success(adminUserInfoService.get(id));
    }

    /**
     * 重置管理员用户密码
     */
    @Log(title = "管理员用户信息-重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd/{id}")
    public Result<String> resetPwd(@PathVariable Long id) {
        log.info("重置管理员用户密码");
        return Result.success(adminUserInfoService.resetPwd(id));
    }

    /**
     * 修改管理员状态
     */
    @Log(title = "管理员用户信息-修改状态", businessType = BusinessType.UPDATE)
    @PutMapping("/updateStatus")
    public Result<String> updateStatus(@RequestBody AdminUserInfoUpdateStatusDTO adminUserInfoUpdateStatusDTO) {
        log.info("修改管理员状态:{}", adminUserInfoUpdateStatusDTO);
        adminUserInfoService.updateStatus(adminUserInfoUpdateStatusDTO);
        return Result.success(SuccessConstant.UPDATE_SUCCESS);
    }
}
