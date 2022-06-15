package com.toby.service;

import com.toby.dao.DataLoaderFromFS;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xiaoxl
 * @date 2022/6/15 11:03
 */
public class DataAnalyseImplTest {
    private DataAnalyse dataAnalyse;
    @Before
    public void setUp() throws Exception {
        dataAnalyse = new DataAnalyseImpl(new DataLoaderFromFS("access.log"));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTotalRequest() {
        System.out.println(dataAnalyse.getTotalRequest());
    }

    @Test
    public void getTopTenRequest() {
        System.out.println(dataAnalyse.getTopTenRequest());
    }

    @Test
    public void getMethodNum() {
        Assert.assertEquals(dataAnalyse.getMethodNum("GET")+ dataAnalyse.getMethodNum("POST"),dataAnalyse.getTotalRequest());
    }

    @Test
    public void classifyByRequestPrefix() {
        System.out.println(dataAnalyse.classifyByRequestPrefix());
    }
}