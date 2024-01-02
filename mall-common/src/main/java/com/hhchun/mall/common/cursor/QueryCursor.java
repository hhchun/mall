package com.hhchun.mall.common.cursor;

import java.util.Collections;
import java.util.Iterator;

public interface QueryCursor<T> extends QueryIterator<T> {
    /**
     * @return the current position of the cursor.
     */
    default long getPosition() {
        return -1;
    }

    static <T> QueryCursor<T> emptyQueryCursor() {
        return new QueryCursor<T>() {
            private final Iterator<T> delegate = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public T next() {
                return delegate.next();
            }
        };
    }
}
