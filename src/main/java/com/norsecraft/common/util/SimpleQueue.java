package com.norsecraft.common.util;

import com.google.common.collect.Lists;

import java.util.List;

public class SimpleQueue<T> {

    private final List<T> list;

    public SimpleQueue() {
        this.list = Lists.newArrayList();
    }

    public void add(T item) {
        this.list.add(item);
    }

    public T peek() {
        return this.list.remove(this.list.size() - 1);
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

}
