package com.toby.util;

/**
 * @author xiaoxl
 * @date 2022/6/15 10:22
 */
public interface Constants {
    /**
     * 请求方法名
     */
    String REQUEST_METHOD = "method";
    /**
     * 请求url格式1
     */
    String REQUEST_URL_PATTERN1 = "/aaa/bbb";
    /**
     * 请求url格式2
     */
    String REQUEST_URL_PATTERN2 = "/aaa/bbb/ccc";
    /**
     * 请求url前缀名
     */
    String REQUEST_URL_PREFIX = "/aaa/";
    /**
     * 请求方法正则表达式
     */
    String REQUEST_METHOD_REGEX = "GET|POST|DELETE|PUT|PATCH|HEAD";
    /**
     * 请求url格式1正则表达式
     */
    String REQUEST_URL_REGEX1 = "/[a-zA-Z]+/[a-zA-Z]+\\.?[a-zA-Z]+";
    /**
     * 请求url格式2正则表达式
     */
    String REQUEST_URL_REGEX2 = "/[a-zA-Z]+/[a-zA-Z]+/[a-zA-Z]+\\.?[a-zA-Z]+";
    /**
     * 请求url前缀名正则表达式
     */
    String REQUEST_URL_PREFIX_REGEX = "/[a-zA-Z]+/";
    /**
     * 分隔符
     */
    String SEPARATOR = "_____";
}
