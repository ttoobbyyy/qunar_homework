package com.toby.dao;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaoxl
 * @date 2022/6/15 15:34
 */
public class DataStorerToFSTest {

    @Test
    public void wirte() {
        DataStorer dataStorer = new DataStorerToFS("hello.txt");
        dataStorer.wirte("hello".getBytes(StandardCharsets.UTF_8));
    }
}