package com.toby.dao;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * @author xiaoxl
 * @date 2022/6/14 17:55
 */
public class DataLoaderFromFSTest {

    @Test
    public void get() {
        DataLoaderFromFS loader = new DataLoaderFromFS("access.log");
        Stream<String> stringStream = loader.get();
        Assert.assertNotNull(stringStream);
    }
}