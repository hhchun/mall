package com.hhchun.mall.common.utils.cursor;

import java.util.Collections;
import java.util.Iterator;

public abstract class AbstractQueryCursor<T> implements QueryCursor<T> {
    protected final QueryOptions options;
    protected QueryState state;
    protected Iterator<T> delegate;
    protected long position;

    public AbstractQueryCursor() {
        this.options = QueryOptions.NONE;
        this.delegate = Collections.emptyIterator();
        this.state = QueryState.READY;
    }

    public AbstractQueryCursor(QueryOptions options) {
        this.options = options;
        this.delegate = Collections.emptyIterator();
        this.state = QueryState.READY;
    }

    @Override
    public long getPosition() {
        return position;
    }

    @Override
    public boolean hasNext() {
        while (!delegate.hasNext() && !QueryState.FINISHED.equals(state)) {
            scan();
        }
        return delegate.hasNext();
    }


    @Override
    public T next() {
        T next = delegate.next();
        position++;
        return next;
    }

    private void scan() {
        doScan();
    }

    protected abstract void doScan();

    public enum QueryState {
        READY, FINISHED, CLOSED;
    }

}
