package com.toby.dao;

import org.junit.Test;

/**
 * @author xiaoxl
 * @date 2022/6/15 14:52
 */
public class DataLoaderFromFSTest {

    @Test
    public void read() {
        long count = new DataLoaderFromFS("StringUtils.java").read().count();
        System.out.println(count);
    }
}