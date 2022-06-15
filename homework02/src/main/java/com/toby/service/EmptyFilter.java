package com.toby.service;

import com.toby.util.StringUtils;

import java.util.stream.Stream;

/**
 * 空行过滤器
 * @author xiaoxl
 * @date 2022/6/15 16:09
 */
public class EmptyFilter implements Filter{
    @Override
    public Stream<String> filter(Stream<String> stream) {
        return stream.filter(StringUtils::isNotBlank);
    }
}
