package com.cm.resource.mapper;

import com.cm.common.aop.annotations.AutoFile;
import com.cm.common.enumeration.OperationType;
import com.cm.dto.ResourceDTO;
import com.cm.dto.ResourceListDTO;
import com.cm.entity.TeachingResource;
import com.cm.entity.User;
import com.cm.vo.ResourceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface ResourceManagementMapper {

    /**
     * 获取用户信息
     * @param currentId 当前用户id
     * @return 用户信息
     */
    User getUserInfoById(Long currentId);

    /**
     * 添加资源
     * @param teachingResource 资源信息
     */
    @AutoFile(OperationType.INSERT)
    void addResource(TeachingResource teachingResource);

    /**
     * 获取资源列表
     * @param resourceListDTO 资源列表参数
     * @return 资源列表
     */
    List<ResourceVO> getResourceList(ResourceListDTO resourceListDTO);

    /**
     * 修改文件夹名称
     * @param resourceDTO 修改的数据
     */
    void updateResource(ResourceDTO resourceDTO);

    /**
     * 删除文件夹/文件
     * @param id 文件夹/文件id
     */
    void deleteResource(Long id);
}
