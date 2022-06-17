package com.toby.dao;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xiaoxl
 * @date 2022/6/17 15:46
 */
public class DataLoaderFromFSTest {

    @Test
    public void read() {
        DataLoader hello = new DataLoaderFromFS("hello");
        hello.read();
    }
}