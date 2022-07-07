package com.toby.sevice.impl;

import com.toby.dao.DataLoader;
import com.toby.dao.DataLoaderFromFS;
import com.toby.entity.Cmd;
import com.toby.sevice.Executor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        if (infos.getParams() == null || !infos.getParams().contains("l") || (input == null && infos.getOtherParams() == null)){
            return null;
        }
        if (input == null){
            DataLoader dataLoader = new DataLoaderFromFS(infos.getOtherParams());
            input = dataLoader.read().collect(Collectors.toList());
        }
        int size = input.size();
        return Collections.singletonList(String.valueOf(size));
    }
}
