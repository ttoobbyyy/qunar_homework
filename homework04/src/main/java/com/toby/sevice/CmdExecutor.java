package com.toby.sevice;

import com.toby.entity.Cmd;

import java.util.List;

/**
 * @author xiaoxl
 * @date 2022/6/17 14:15
 */
public interface CmdExecutor {
    /**
     * 执行命令
     * @param cmds
     * @return
     * @throws Exception
     */
    boolean executorCmds(List<Cmd> cmds) throws Exception;
}
