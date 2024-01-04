package com.hhchun.mall.bootstrap;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.hhchun.mall.user.platform.dao.PlatformMenuDao;
import com.hhchun.mall.user.platform.entity.domain.PlatformPermissionEntity;
import com.hhchun.mall.user.platform.entity.vo.PlatformMenuVo;
import com.hhchun.mall.user.platform.service.PlatformMenuService;
import com.hhchun.mall.user.platform.service.PlatformPermissionService;
import com.hhchun.mall.user.platform.service.PlatformRolePermissionService;
import com.hhchun.mall.user.platform.service.PlatformUserRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DeleteTest {

    @Autowired
    private PlatformPermissionService platformPermissionService;
    @Autowired
    private PlatformRolePermissionService platformRolePermissionService;

    @Autowired
    private PlatformUserRoleService platformUserRoleService;
    @Autowired
    private PlatformMenuDao platformMenuDao;

    @Autowired
    private PlatformMenuService platformMenuService;

    @Test
    public void test() {
        List<PlatformPermissionEntity> list = platformPermissionService.list(new LambdaQueryWrapper<PlatformPermissionEntity>()
                .eq(PlatformPermissionEntity::getDel, 1));
        System.out.println(list);
    }

    @Test
    public void test1() {
        List<Long> ids = platformRolePermissionService.getPlatformRemovedRoleIdsByPermissionId(0L);
        System.out.println(ids);
    }

    @Test
    public void test2() {
        List<Long> ids = platformUserRoleService.getPlatformRemovedUserIdsByRoleIds(Lists.newArrayList(0L, 1L));
        System.out.println(ids);
    }

    @Test
    public void test3() {
        platformMenuDao.replaceRoute("old", "new");
    }

    @Test
    public void test4() {
        List<PlatformMenuVo> platformMenusByUserId = platformMenuService.getPlatformMenusByUserId(1L, true, true);

    }
}
