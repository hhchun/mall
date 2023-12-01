package com.hhchun.mall.access.platform.service.impl;

import com.hhchun.mall.access.platform.dao.PlatformUserDao;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.platform.entity.domain.PlatformUserEntity;
import com.hhchun.mall.access.platform.service.PlatformUserService;


@Service("platformUserService")
public class PlatformUserServiceImpl extends ServiceImpl<PlatformUserDao, PlatformUserEntity> implements PlatformUserService {

}