package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.platform.dao.PlatformMenuPermissionDao;
import org.springframework.stereotype.Service;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuPermissionEntity;
import com.hhchun.mall.access.platform.service.PlatformMenuPermissionService;


@Service("platformMenuPermissionService")
public class PlatformMenuPermissionServiceImpl extends ServiceImpl<PlatformMenuPermissionDao, PlatformMenuPermissionEntity> implements PlatformMenuPermissionService {

}