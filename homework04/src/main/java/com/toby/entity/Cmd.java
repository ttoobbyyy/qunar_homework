package com.toby.entity;

import lombok.Data;

import java.util.List;

/**
 * @author xiaoxl
 * @date 2022/6/16 17:48
 */
@Data
public class Cmd {
    /**
     * 命令名
     */
    private String name;
    /**
     * 命令参数
     */
    private List<String> params;
    /**
     * 其他参数
     */
    private String otherParams;
    /**
     * 是否是最后一个命令
     */
    private Boolean isEnd = false;
}
