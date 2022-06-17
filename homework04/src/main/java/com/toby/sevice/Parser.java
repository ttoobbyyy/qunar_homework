package com.toby.sevice;

import com.toby.entity.Cmd;

import java.util.List;

/**
 * @author xiaoxl
 * @date 2022/6/16 17:59
 */
public interface Parser {
    List<Cmd> parse(String cmd);
}
