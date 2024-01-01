package com.hhchun.mall.access.platform.dao;

import com.hhchun.mall.access.platform.entity.domain.PlatformMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 平台菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@Mapper
public interface PlatformMenuDao extends BaseMapper<PlatformMenuEntity> {

    /**
     * 用新的route替换所有旧的route
     *
     * @param o 旧的route
     * @param n 新的route
     */
    void replaceRoute(@Param("o") String o, @Param("n") String n);
}
