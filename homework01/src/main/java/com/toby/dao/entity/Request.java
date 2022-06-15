package com.toby.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 每个请求的实体类，从完成作业的角度来说，定义实体类是会损耗性能的，但定义实体类可以增加程序的扩展性
 * 实体类的属性目前只包含题目中需要求的
 * @author xiaoxl
 * @date 2022/6/14 19:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    /**
     * 请求路径
     */
    private String path;
    /**
     * 请求方法类型
     */
    private String method;
    /**
     * 同路径的请求数量
     */
    private Long num;
    /**
     * 请求路径前缀
     */
    private String prePath;
    /**
     * 请求路径前缀分类下，具体的url
     */
    private Set<String> urls;
}
