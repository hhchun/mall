package com.hhchun.mall.common.cursor;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class MybatisPlusQueryCursor<T> extends AbstractQueryCursor<T> {
    private final long size;
    private long current;

    private final IService<T> service;
    private final Wrapper<T> wrapper;

    public MybatisPlusQueryCursor(IService<T> service, Wrapper<T> wrapper, QueryOptions options) {
        super(options);
        this.size = options.getCount();
        this.current = 1L;
        this.service = service;
        this.wrapper = wrapper;
    }

    public MybatisPlusQueryCursor(IService<T> service, QueryOptions options) {
        this(service, null, options);
    }

    @Override
    protected void doScan() {
        Page<T> page = service.page(new Page<>(current, size), wrapper);
        List<T> items = page.getRecords();
        if (!CollectionUtils.isEmpty(items)) {
            super.delegate = items.iterator();
            current++;
        } else {
            delegate = Collections.emptyIterator();
            super.state = AbstractQueryCursor.QueryState.FINISHED;
        }
    }


}
