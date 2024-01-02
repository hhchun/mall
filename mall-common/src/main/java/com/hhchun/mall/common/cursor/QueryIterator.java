package com.hhchun.mall.common.cursor;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface QueryIterator<T> extends Iterator<T> {

    default Spliterator<T> spliterator() {
        return new QueryIteratorSpliterator<>(this);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
