package com.toby.service;

import com.toby.dao.entity.Request;

import java.util.List;

/**
 * 数据的处理函数，对应题目的每个小问
 * @author xiaoxl
 * @date 2022/6/14 19:34
 */
public interface DataAnalyse {
    /**
     * 返回请求总量
     * @return
     */
    long getTotalRequest();

    /**
     * 获取请求top10频繁的接口
     * @return
     */
    List<Request> getTopTenRequest();

    /**
     * 获取某个请求方法的请求数量
     * @param method
     * @return
     */
    long getMethodNum(String method);

    /**
     * 根据请求路径的前缀对请求进行分类
     * @return
     */
    List<Request> classifyByRequestPrefix();
}
