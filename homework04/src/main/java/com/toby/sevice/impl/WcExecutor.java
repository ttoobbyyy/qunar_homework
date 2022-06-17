package com.toby.sevice.impl;

import com.toby.entity.Cmd;
import com.toby.sevice.Executor;

import java.util.Collections;
import java.util.List;

/**
 * @author xiaoxl
 * @date 2022/6/16 17:56
 */
public class WcExecutor implements Executor {
    private Cmd infos;
    public WcExecutor(Cmd infos){
        this.infos = infos;
    }
    @Override
    public List<String> execute(List<String> input) {
        if (input == null || infos.getParams() == null || !infos.getParams().contains("l")){
            return null;
        }
        int size = input.size();
        return Collections.singletonList(String.valueOf(size));
    }
}
