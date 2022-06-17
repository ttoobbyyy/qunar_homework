package com.toby.sevice.impl;

import com.toby.entity.Cmd;
import com.toby.sevice.Executor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoxl
 * @date 2022/6/16 17:58
 */
public class GrepExecutor implements Executor {
    private Cmd infos;
    public GrepExecutor(Cmd infos){
        this.infos = infos;
    }
    @Override
    public List<String> execute(List<String> input) {
        if (input == null || infos.getOtherParams() == null){
            return null;
        }
       return input.stream().filter(line-> line.contains(infos.getOtherParams())).collect(Collectors.toList());
    }
}
