package com.hhchun.mall.common.cursor;

import org.springframework.lang.Nullable;

public class QueryOptions {
    public static QueryOptions NONE = new QueryOptions(null);

    private static final Long DEFAULT_SIZE = 1L << 10L;

    private final Long count;

    private QueryOptions(@Nullable Long count) {
        if (count == null || count <= 0) {
            count = DEFAULT_SIZE;
        }
        this.count = count;
    }

    public static QueryOptions.QueryOptionsBuilder options() {
        return new QueryOptions.QueryOptionsBuilder();
    }

    public Long getCount() {
        return count;
    }

    public static class QueryOptionsBuilder {

        @Nullable
        Long count;

        QueryOptionsBuilder() {
        }

        public QueryOptions.QueryOptionsBuilder count(long count) {
            this.count = count;
            return this;
        }

        public QueryOptions build() {

            return new QueryOptions(count);
        }
    }
}
