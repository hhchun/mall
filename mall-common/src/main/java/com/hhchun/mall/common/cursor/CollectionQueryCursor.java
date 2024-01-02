package com.hhchun.mall.common.cursor;

import java.util.Collection;
import java.util.Iterator;

public class CollectionQueryCursor<T> implements QueryCursor<T> {
    protected final Iterator<T> it;
    private long position;

    public CollectionQueryCursor(Iterator<T> it) {
        this.it = it;
    }

    public CollectionQueryCursor(Collection<T> collection) {
        this.it = collection.iterator();
    }

    @Override
    public long getPosition() {
        return position;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        position++;
        return it.next();
    }
}
