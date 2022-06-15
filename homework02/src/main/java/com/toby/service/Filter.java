package com.toby.service;

import java.util.stream.Stream;

/**
 * 过滤算子的接口
 * 感觉是有点过度设计了
 * @author xiaoxl
 * @date 2022/6/15 15:05
 */
public interface Filter {
    Stream<String> filter(Stream<String> stream);
}
