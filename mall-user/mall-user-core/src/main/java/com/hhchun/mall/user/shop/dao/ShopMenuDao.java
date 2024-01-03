package com.hhchun.mall.user.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhchun.mall.user.shop.entity.domain.ShopMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 店铺菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@Mapper
public interface ShopMenuDao extends BaseMapper<ShopMenuEntity> {

    /**
     * 用新的route替换所有旧的route
     *
     * @param o 旧的route
     * @param n 新的route
     */
    void replaceRoute(@Param("o") String o, @Param("n") String n);
}
