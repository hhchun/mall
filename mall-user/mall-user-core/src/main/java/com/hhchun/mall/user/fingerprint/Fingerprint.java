package com.hhchun.mall.user.fingerprint;

/**
 * 用户指纹
 */
public class Fingerprint {
    // 平台端
    public static final String CLIENT_TYPE_PLATFORM = "platform";
    // 门店端
    public static final String CLIENT_TYPE_SHOP = "shop";
    // 顾客端
    public static final String CLIENT_TYPE_CUSTOMER = "customer";

    /**
     * 客户端类型
     *
     * @see Fingerprint#CLIENT_TYPE_PLATFORM
     * @see Fingerprint#CLIENT_TYPE_SHOP
     * @see Fingerprint#CLIENT_TYPE_CUSTOMER
     */
    private String clientType;

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
