package com.hhchun.mall.access.platform.service.impl;

import com.hhchun.mall.access.platform.dao.RolePermissionDao;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.platform.entity.domain.RolePermissionEntity;
import com.hhchun.mall.access.platform.service.RolePermissionService;


@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionDao, RolePermissionEntity> implements RolePermissionService {

}