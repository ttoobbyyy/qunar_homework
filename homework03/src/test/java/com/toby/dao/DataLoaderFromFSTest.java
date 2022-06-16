package com.toby.dao;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xiaoxl
 * @date 2022/6/16 9:14
 */
public class DataLoaderFromFSTest {

    @Test
    public void read() {
        DataLoaderFromFS dataLoaderFromFS = new DataLoaderFromFS("sdxl_template.txt");
        dataLoaderFromFS.read().forEach(System.out::println);
    }
}