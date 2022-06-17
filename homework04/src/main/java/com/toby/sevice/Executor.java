package com.toby.sevice;

import java.util.List;

/**
 * 命令的接口
 * @author xiaoxl
 * @date 2022/6/16 17:50
 */
public interface Executor {
    List<String> execute(List<String> input);
}
