package com.hhchun.mall.common.utils.cursor;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

class QueryIteratorSpliterator<T> implements Spliterator<T> {
    private static final int BATCH_UNIT = 1 << 10;
    private static final int MAX_BATCH = 1 << 25;
    private final Iterator<? extends T> it;
    private long est;
    private int batch;

    public QueryIteratorSpliterator(Iterator<? extends T> iterator) {
        this.it = iterator;
        this.est = Long.MAX_VALUE;
    }

    @Override
    public Spliterator<T> trySplit() {
        Iterator<? extends T> i = it;
        long s = est;
        if (s > 1 && i.hasNext()) {
            int n = batch + BATCH_UNIT;
            if (n > s) {
                n = (int) s;
            }
            if (n > MAX_BATCH) {
                n = MAX_BATCH;
            }
            Object[] a = new Object[n];
            int j = 0;
            do {
                a[j] = i.next();
            } while (++j < n && i.hasNext());
            batch = j;
            if (est != Long.MAX_VALUE) {
                est -= j;
            }
            return Spliterators.spliterator(a, 0, j, 0);
        }
        return null;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        it.forEachRemaining(action);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (it.hasNext()) {
            action.accept(it.next());
            return true;
        }
        return false;
    }

    @Override
    public long estimateSize() {
        return -1;
    }

    @Override
    public int characteristics() {
        return 0;
    }

    @Override
    public Comparator<? super T> getComparator() {
        if (hasCharacteristics(Spliterator.SORTED)) {
            return null;
        }
        throw new IllegalStateException();
    }
}
