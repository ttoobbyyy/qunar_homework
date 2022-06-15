package com.toby.service;

import java.util.stream.Stream;

/**
 * 注释过滤器
 * @author xiaoxl
 * @date 2022/6/15 16:15
 */
public class CommentFilter implements Filter{
    @Override
    public Stream<String> filter(Stream<String> stream) {
        return stream.filter(this::isNotComment);
    }

    private boolean isNotComment(String line) {
        line = line.trim();
        return !(line.startsWith("*") || line.startsWith("/") || line.startsWith("//"));
    }
}
