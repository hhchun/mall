package com.hhchun.mall.user.shop.entity.vo;

import com.google.common.collect.Lists;
import com.hhchun.mall.user.shop.constant.ShopMenuConstant;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 店铺菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@Data
public class ShopMenuVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 菜单标识
     */
    private String symbol;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 类型
     *
     * @see ShopMenuConstant#MENU_TYPE_MENU
     * @see ShopMenuConstant#MENU_TYPE_OPERATE
     */
    private Integer type;
    /**
     * 描述/备注
     */
    private String des;
    /**
     * 父菜单id
     */
    private Long pid;
    /**
     * 公开的菜单,0-否、1-是
     */
    private Integer overt;
    /**
     * 下级菜单
     */
    private List<ShopMenuVo> children;

    /**
     * 多级菜单进行组合
     *
     * @param mvs 菜单
     * @return 组合完成的菜单
     */
    public static List<ShopMenuVo> combine(List<ShopMenuVo> mvs) {
        if (CollectionUtils.isEmpty(mvs)) {
            return Lists.newArrayList();
        }
        List<ShopMenuVo> roots = Lists.newArrayList();
        Map<Long, ShopMenuVo> mvMap = mvs.stream().collect(Collectors.toMap(ShopMenuVo::getId, mv -> mv));
        for (ShopMenuVo mv : mvs) {
            Long pid = mv.getPid();
            if (ShopMenuConstant.MENU_ROOT_PID.equals(pid)) {
                roots.add(mv);
            } else {
                ShopMenuVo pmv = mvMap.get(pid);
                if (pmv == null) {
                    // 除非数据错乱否则不可能出现为空的情况
                    continue;
                }
                List<ShopMenuVo> children = pmv.getChildren();
                if (children == null) {
                    children = Lists.newArrayList();
                    pmv.setChildren(children);
                }
                children.add(mv);
            }
        }
        return roots;
    }


}
