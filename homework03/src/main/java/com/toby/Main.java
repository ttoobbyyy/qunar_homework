package com.toby;

import com.toby.dao.DataLoader;
import com.toby.dao.DataLoaderFromFS;
import com.toby.dao.DataStorer;
import com.toby.dao.DataStorerToFS;
import com.toby.service.DataProcess;
import com.toby.service.DataProcessImpl;

/**
 * @author xiaoxl
 * @date 2022/6/16 10:15
 */
public class Main {
    private static final String FILE_PROP = "sdxl_prop.txt";
    private static final String FILE_TEMPLATE = "sdxl_template.txt";
    private static final String FILE_RESULT = "sdxl.txt";

    public static void main(String[] args) {
        DataLoader prop = new DataLoaderFromFS(FILE_PROP);
        DataLoader template = new DataLoaderFromFS(FILE_TEMPLATE);
        DataStorer storer = new DataStorerToFS(FILE_RESULT);

        DataProcess dataProcess = new DataProcessImpl(prop,template,storer);

        dataProcess.process();
        System.out.println("success!!!");
    }
}
