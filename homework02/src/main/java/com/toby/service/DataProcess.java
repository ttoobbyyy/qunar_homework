package com.toby.service;

import java.util.stream.Stream;

/**
 * 数据处理接口
 * @author xiaoxl
 * @date 2022/6/15 15:09
 */
public interface DataProcess {
    /**
     * 增加过滤算子
     * @param filter
     */
    DataProcess add(Filter filter);

    /**
     * 处理数据
     * @return
     */
    Stream<String> process();
}
