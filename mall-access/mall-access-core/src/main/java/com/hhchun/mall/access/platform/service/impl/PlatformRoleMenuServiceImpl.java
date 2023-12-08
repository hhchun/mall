package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.platform.dao.PlatformRoleMenuDao;
import org.springframework.stereotype.Service;

import com.hhchun.mall.access.platform.entity.domain.PlatformRoleMenuEntity;
import com.hhchun.mall.access.platform.service.PlatformRoleMenuService;


@Service("platformRoleMenuService")
public class PlatformRoleMenuServiceImpl extends ServiceImpl<PlatformRoleMenuDao, PlatformRoleMenuEntity> implements PlatformRoleMenuService {

}