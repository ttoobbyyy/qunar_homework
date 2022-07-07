package com.toby.sevice.impl;

import com.toby.dao.DataLoader;
import com.toby.dao.DataLoaderFromFS;
import com.toby.entity.Cmd;
import com.toby.sevice.Executor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoxl
 * @date 2022/6/16 17:52
 */
public class CatExecutor implements Executor {
    private Cmd infos;
    public CatExecutor(Cmd infos){
        this.infos = infos;
    }
    @Override
    public List<String> execute(List<String> input) {
        if (infos.getOtherParams() == null){
            return null;
        }
        DataLoader dataLoader = new DataLoaderFromFS(infos.getOtherParams());
        return dataLoader.read().collect(Collectors.toList());
    }
}
