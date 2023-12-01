package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.platform.dao.PlatformRoleDao;
import org.springframework.stereotype.Service;
import com.hhchun.mall.access.platform.entity.domain.PlatformRoleEntity;
import com.hhchun.mall.access.platform.service.PlatformRoleService;


@Service("platformRoleService")
public class PlatformRoleServiceImpl extends ServiceImpl<PlatformRoleDao, PlatformRoleEntity> implements PlatformRoleService {

}