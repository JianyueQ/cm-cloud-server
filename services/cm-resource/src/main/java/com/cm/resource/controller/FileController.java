package com.cm.resource.controller;

import cn.hutool.core.lang.UUID;
import com.cm.common.alibabaOSS.utils.AliOssUtil;
import com.cm.common.core.constant.MessageConstant;
import com.cm.common.core.exception.CommonQuestionException;
import com.cm.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 31373
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    private final AliOssUtil aliOssUtil;

    public FileController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    /**
     * 实现图片上传
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {

        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        log.info("图片上传开始,上传的文件名为:{}", originalFilename);
        //判断文件名的后缀
        String[] split = originalFilename.split("\\.");
        String suffix = split[split.length - 1];
        if (!"png".equals(suffix) && !"jpg".equals(suffix) && !"jpeg".equals(suffix)) {
            //文件格式错误
            throw new CommonQuestionException(MessageConstant.FILE_FORMAT_ERROR);
        }
        //生成新的文件名称
        String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //调用阿里云oss工具类
        try {
            String upload = aliOssUtil.upload(file.getBytes(), newFileName);
            log.info("图片上传成功,上传的文件地址为:{}", upload);
            return Result.success(upload);
        } catch (IOException e) {
            //文件上传失败
            log.error(e.getMessage());
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

    /**
     * 实现视频上传
     */
    @PostMapping("/uploadVideo")
    public Result<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        log.info("视频上传开始,上传的文件名为:{}", originalFilename);
        //判断文件名的后缀
        String[] split = originalFilename.split("\\.");
        String suffix = split[split.length - 1];
        if (!"mp4".equals(suffix) && !"avi".equals(suffix) && !"mkv".equals(suffix)) {
            //文件格式错误
            throw new CommonQuestionException(MessageConstant.FILE_FORMAT_ERROR);
        }
        //生成新的文件名称
        String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //调用阿里云oss工具类
        try {
            String upload = aliOssUtil.upload(file.getBytes(), newFileName);
            log.info("视频上传成功,上传的文件地址为:{}", upload);
            return Result.success(upload);
        } catch (IOException e) {
            //文件上传失败
            log.error(e.getMessage());
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

    /**
     * 教学资源多文件上传
     * 资源类型: 1:课件, 2:视频, 3:文档, 4:链接, 5:其他
     */
    @PostMapping("/uploadResource")
    public Result<List<String>> uploadResource(@RequestParam("files") MultipartFile[] files,
                                               @RequestParam("type") Integer type) {
        log.info("教学资源上传开始, 文件数量:{}, 资源类型:{}", files.length, type);

        // 验证资源类型
        if (type < 1 || type > 5) {
            throw new CommonQuestionException(MessageConstant.FILE_FORMAT_ERROR);
        }

        List<String> fileUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                // 获取文件名称
                String originalFilename = file.getOriginalFilename();
                log.info("正在上传文件:{}", originalFilename);

                // 生成新的文件名称
                String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

                // 根据资源类型确定文件路径前缀
                String prefix = getResourceTypePrefix(type);
                newFileName = prefix + "/" + newFileName;

                // 调用阿里云oss工具类上传文件
                String upload = aliOssUtil.upload(file.getBytes(), newFileName);
                fileUrls.add(upload);
                log.info("文件上传成功,上传的文件地址为:{}", upload);
            }

            return Result.success(fileUrls);
        } catch (IOException e) {
            // 文件上传失败
            log.error("文件上传失败: {}", e.getMessage());
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

    /**
     * 删除OSS上的文件
     */
    @PostMapping("/deleteResource")
    public Result<String> deleteResource(@RequestParam("fileName") String fileName) {
        log.info("开始删除文件: {}", fileName);
        try {
            // 从文件URL中提取文件名
            String objectName = getObjectKeyFromUrl(fileName);
            aliOssUtil.delete(objectName);
            return Result.success("文件删除成功");
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage());
            return Result.error("文件删除失败");
        }
    }

    /**
     * 根据资源类型获取路径前缀
     * @param type 资源类型 1:课件, 2:视频, 3:文档, 4:链接, 5:其他
     * @return 路径前缀
     */
    private String getResourceTypePrefix(Integer type) {
        return switch (type) {
            case 1 -> "courseware";
            case 2 -> "video";
            case 3 -> "document";
            case 4 -> "link";
            default -> "other";
        };
    }

    /**
     * 从URL中提取OSS对象键
     * @param url 文件URL
     * @return 对象键
     */
    private String getObjectKeyFromUrl(String url) {
        // 从URL中提取文件名，例如从 https://bucket.endpoint/objectName 提取 objectName
        int index = url.indexOf("/", url.indexOf("//") + 2);
        if (index != -1) {
            return url.substring(index + 1);
        }
        return url;
    }
}
