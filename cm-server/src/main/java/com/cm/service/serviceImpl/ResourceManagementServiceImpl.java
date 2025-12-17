package com.cm.service.serviceImpl;

import com.cm.annotations.RedisCache;
import com.cm.annotations.RedisCacheEvict;
import com.cm.constant.TypeConstant;
import com.cm.context.BaseContext;
import com.cm.dto.ResourceDTO;
import com.cm.dto.ResourceListDTO;
import com.cm.entity.TeachingResource;
import com.cm.entity.User;
import com.cm.mapper.ResourceManagementMapper;
import com.cm.service.ResourceManagementService;
import com.cm.utils.AliOssUtil;
import com.cm.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31373
 */
@Service
public class ResourceManagementServiceImpl implements ResourceManagementService {

    @Autowired
    private ResourceManagementMapper resourceManagementMapper;
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 添加文件夹/文件
     *
     * @param resourceDTO 文件夹/文件信息
     */
    @Override
    @RedisCacheEvict(@RedisCacheEvict.CacheKeyConfig(keyPrefix = "resourceList:", isPattern = true))
    public void addResource(ResourceDTO resourceDTO) {
        Long currentId = BaseContext.getCurrentId();
        //获取上传人的信息
        User user = resourceManagementMapper.getUserInfoById(currentId);
        TeachingResource teachingResource = new TeachingResource();
        if (resourceDTO.getResourceType().equals(TypeConstant.RESOURCE_TYPE_FOLDER)) {
            //文件夹名称
            teachingResource.setTitle(resourceDTO.getTitle());
            //文件夹描述
            teachingResource.setDescription(resourceDTO.getDescription());
            //文件类型
            teachingResource.setResourceType(resourceDTO.getResourceType());
            //父级目录id
            teachingResource.setParentId(resourceDTO.getParentId());
            //课程id
            teachingResource.setCourseId(resourceDTO.getCourseId());
            //课程名称
            teachingResource.setCourseName(resourceDTO.getCourseName());
            //上传者id
            teachingResource.setUploaderId(user.getId());
            //上传者名称
            teachingResource.setUploaderName(user.getRealName());
            resourceManagementMapper.addResource(teachingResource);
        } else {
            //处理多个文件的情况
            String[] fileUrls = resourceDTO.getFileUrls().split(",");
            String[] fileNames = resourceDTO.getFileNames().split(",");
            String[] fileSizes = resourceDTO.getFileSizes().split(",");
            //确保数组长度一致
            int fileCount = fileUrls.length;
            if (fileNames.length != fileCount || fileSizes.length != fileCount) {
                //处理不一致的情况，可以抛出异常或记录日志
                throw new IllegalArgumentException("文件信息不一致");
            }
            //为每个文件创建一条记录
            for (int i = 0; i < fileCount; i++) {
                TeachingResource fileResource = new TeachingResource();
                fileResource.setTitle(resourceDTO.getTitle());
                fileResource.setDescription(resourceDTO.getDescription());
                fileResource.setResourceType(resourceDTO.getResourceType());
                fileResource.setCourseId(resourceDTO.getCourseId());
                fileResource.setCourseName(resourceDTO.getCourseName());
                fileResource.setParentId(resourceDTO.getParentId());
                //每个文件只对应一个URL、文件名和文件大小
                fileResource.setFileUrls(fileUrls[i]);
                fileResource.setFileNames(fileNames[i]);
                fileResource.setFileSizes(fileSizes[i]);
                //上传者id
                fileResource.setUploaderId(user.getId());
                //上传者名称
                fileResource.setUploaderName(user.getRealName());
                resourceManagementMapper.addResource(fileResource);
            }
        }
    }

    /**
     * 获取文件夹/文件列表
     *
     * @param resourceListDTO 文件夹/文件列表参数
     * @return 文件夹/文件列表
     */
    @Override
    @RedisCache(keyPrefix = "resourceList:",
            keyParts = {"#resourceListDTO.courseId", "#resourceListDTO.parentId", "#resourceListDTO.title", "#resourceListDTO.title"})
    public List<ResourceVO> getResourceList(ResourceListDTO resourceListDTO) {
        return resourceManagementMapper.getResourceList(resourceListDTO);
    }

    /**
     * 修改文件夹信息
     *
     * @param resourceDTO 文件夹信息
     */
    @RedisCacheEvict(@RedisCacheEvict.CacheKeyConfig(keyPrefix = "resourceList:", isPattern = true))
    @Override
    public void updateResource(ResourceDTO resourceDTO) {
        resourceManagementMapper.updateResource(resourceDTO);
    }

    /**
     * 删除文件夹/文件
     *
     * @param id       文件夹/文件id
     * @param fileUrls 文件路径
     */
    @RedisCacheEvict(@RedisCacheEvict.CacheKeyConfig(keyPrefix = "resourceList:", isPattern = true))
    @Override
    public void deleteResource(Long id, String fileUrls) {
        resourceManagementMapper.deleteResource(id);
        // 从OSS上删除文件
        if (fileUrls != null && !fileUrls.isEmpty()) {
            // 分割文件URL字符串
            String[] urls = fileUrls.split(",");
            for (String url : urls) {
                // 从URL中提取OSS对象名称
                String objectName = getObjectKeyFromUrl(url.trim());
                // 删除OSS上的文件
                aliOssUtil.delete(objectName);

            }
        }
    }

    /**
     * 从文件URL中提取OSS对象名称
     *
     * @param fileUrl 完整的文件URL
     * @return OSS对象名称
     */
    private String getObjectKeyFromUrl(String fileUrl) {
        // 找到域名后的路径部分
        int index = fileUrl.indexOf("/", fileUrl.indexOf("//") + 2);
        if (index != -1) {
            return fileUrl.substring(index + 1);
        }
        return fileUrl;
    }
}
