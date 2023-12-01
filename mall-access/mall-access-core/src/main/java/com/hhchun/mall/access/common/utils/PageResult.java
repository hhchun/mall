package com.hhchun.mall.access.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private Long totalCount;
    /**
     * 每页记录数
     */
    private Long pageSize;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 当前页码
     */
    private Long currPage;
    /**
     * 列表数据
     */
    private List<T> list;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public PageResult(List<T> list, Long totalCount, Long pageSize, Long currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (long) Math.ceil((double) totalCount / pageSize);
    }

    public PageResult(IPage<T> page) {
        this(page.getRecords(), page.getSize(), page.getCurrent(), page.getPages());
    }

    public PageResult() {
        this.list = Lists.newArrayList();
        this.totalCount = 0L;
        this.totalPage = 0L;
        this.pageSize = PageArguments.DEFAULT_PAGE_SIZE;
        this.currPage = PageArguments.DEFAULT_CURR_PAGE;
    }

    public static <T> PageResult<T> convert(IPage<?> page, List<T> list) {
        return new PageResult<>(list, page.getTotal(), page.getSize(), page.getCurrent());
    }

    public static <T> PageResult<T> convert(PageResult<?> page, List<T> list) {
        return new PageResult<>(list, page.getTotalCount(), page.getPageSize(), page.getCurrPage());
    }
}
