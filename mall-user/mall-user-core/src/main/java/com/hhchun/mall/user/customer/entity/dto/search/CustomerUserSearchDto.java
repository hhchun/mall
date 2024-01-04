package com.hhchun.mall.user.customer.entity.dto.search;

import com.hhchun.mall.common.utils.PageArguments;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerUserSearchDto extends PageArguments implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;

}
