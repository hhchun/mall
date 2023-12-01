package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.platform.dao.PlatformPermissionDao;
import org.springframework.stereotype.Service;
import com.hhchun.mall.access.platform.entity.domain.PlatformPermissionEntity;
import com.hhchun.mall.access.platform.service.PlatformPermissionService;


@Service("platformPermissionService")
public class PlatformPermissionServiceImpl extends ServiceImpl<PlatformPermissionDao, PlatformPermissionEntity> implements PlatformPermissionService {

}