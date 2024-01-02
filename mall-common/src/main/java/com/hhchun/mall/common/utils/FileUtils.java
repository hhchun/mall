package com.hhchun.mall.common.utils;

import cn.hutool.system.SystemUtil;
import cn.hutool.system.UserInfo;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

public class FileUtils {

    public static String getSourcePath(Class<?> aClass) {
        ApplicationHome home = new ApplicationHome(aClass);
        return home.getSource().getParent();
    }

    /**
     * 获取服务器映射的路径
     */
    public static String getServerPhysicsPath(String relativePath) {
        if (UnitTest.isUnitTest()) {
            // Unit Test
            UserInfo userInfo = SystemUtil.getUserInfo();
            return new File(userInfo.getTempDir(), relativePath).getPath().replace("\\", "/");
        } else {
            return new File(getSourcePath(FileUtils.class), relativePath).getPath().replace("\\", "/");
        }
    }
}

