package com.hhchun.mall.access.common.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import com.hhchun.mall.access.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

@Slf4j
public class TokenUtils {
    private static final String TOKEN_SECRET_KEY = "hhchun";

    public static String getToken(Long userId) {
        Preconditions.checkArgument(userId != null, "userId == null!");
        String iv = RandomUtil.randomString(8);
        DES des = new DES(Mode.CTS, Padding.ZeroPadding, TOKEN_SECRET_KEY.getBytes());
        des.setIv(iv.getBytes());
        String ciphertext = des.encryptHex(String.valueOf(userId));
        return ciphertext + "." + iv;
    }

    @Nullable
    public static Long getUserId(@Nullable String token) throws CryptoException {
        if (!StringUtils.hasLength(token)) {
            return null;
        }
        String[] split = token.split("\\.");
        if (split.length != 2) {
            return null;
        }
        String iv = split[1];
        String ciphertext = split[0];
        DES des = new DES(Mode.CTS, Padding.ZeroPadding, TOKEN_SECRET_KEY.getBytes());
        des.setIv(iv.getBytes());
        try {
            String plaintext = des.decryptStr(ciphertext);
            if (!StringUtils.hasLength(plaintext)) {
                return null;
            }
            return Long.valueOf(plaintext);
        } catch (CryptoException e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
