package com.toby.sevice.impl;

import com.toby.entity.Cmd;
import com.toby.sevice.CmdExecutor;
import com.toby.sevice.Executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author xiaoxl
 * @date 2022/6/17 14:18
 */
public class CmdExecutorImpl implements CmdExecutor {
    private static final String CAT = "cat";
    private static final String GREP = "grep";
    private static final String WC = "wc";

    private final List<Executor> executors;
    public CmdExecutorImpl(){
        this.executors = new ArrayList<>();
    }
    @Override
    public boolean executorCmds(List<Cmd> cmds) throws Exception{
        if (cmds == null){
            return false;
        }
        // 封装命令执行算子
        initCmds(cmds);
        // 具体执行命令
        List<String> result = execute();
        if (result == null){
            return false;
        }
        // 控制台打印结果
        result.forEach(System.out::println);
        return true;
    }

    private List<String> execute() {
        List<String> stream = null;
        for (Executor executor : executors){
            stream = executor.execute(stream);
        }
        return stream;
    }

    /**
     * 封装命令执行算子
     */
    private void initCmds(List<Cmd> cmds) throws Exception{
        for (Cmd cmd : cmds){
            switch (cmd.getName().toLowerCase(Locale.ROOT)){
                case CAT:
                    executors.add(new CatExecutor(cmd));
                    break;
                case GREP:
                    executors.add(new GrepExecutor(cmd));
                    break;
                case WC:
                    executors.add(new WcExecutor(cmd));
                    break;
                default:
                    throw new Exception("don't support cmd '"+cmd.getName()+"'");
            }
        }
    }
}
