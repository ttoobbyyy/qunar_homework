package com.toby.service;

import com.toby.dao.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author xiaoxl
 * @date 2022/6/15 16:23
 */
public class DataProcessImpl implements DataProcess{
    /**
     * 过滤算子集合
     */
    private final List<Filter> filters;

    /**
     * 数据输入
     */
    private final DataLoader dataLoader;

    public DataProcessImpl(DataLoader dataLoader){
        this.filters = new ArrayList<>();
        this.dataLoader = dataLoader;
    }
    @Override
    public DataProcess add(Filter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public Stream<String> process() {
        Stream<String> stream = dataLoader.read();
        for (Filter filter : filters){
            stream = filter.filter(stream);
        }
        return stream;
    }
}
