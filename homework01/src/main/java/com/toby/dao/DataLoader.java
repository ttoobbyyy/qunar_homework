package com.toby.dao;

import java.util.stream.Stream;

/**
 * 获得需要处理的数据流，屏蔽数据源差异
 * @author xiaoxl
 * @date 2022/6/14 17:36
 */
public interface DataLoader {
    /**
     * 获得操作的数据流
     */
    Stream<String> get();
}
