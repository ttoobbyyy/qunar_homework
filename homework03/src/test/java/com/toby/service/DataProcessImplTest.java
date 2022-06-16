package com.toby.service;

import com.toby.dao.DataLoader;
import com.toby.dao.DataLoaderFromFS;
import com.toby.dao.DataStorer;
import com.toby.dao.DataStorerToFS;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xiaoxl
 * @date 2022/6/16 10:44
 */
public class DataProcessImplTest {
    DataProcess dataProcess;
    @Before
    public void begin(){
        DataLoader dataLoader = new DataLoaderFromFS("sdxl_prop.txt");
        DataLoader dataLoader1 = new DataLoaderFromFS("sdxl_template.txt");
        DataStorer dataStorerToFS = new DataStorerToFS("sdxl.txt");
        dataProcess = new DataProcessImpl(dataLoader,dataLoader1,dataStorerToFS);
    }
    @Test
    public void process() {
        dataProcess.process();
    }

    @Test
    public void save() {
    }
}