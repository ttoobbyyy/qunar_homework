package com.toby;

import com.toby.dao.DataLoader;
import com.toby.dao.DataLoaderFromFS;
import com.toby.dao.DataStorer;
import com.toby.dao.DataStorerToFS;
import com.toby.service.CommentFilter;
import com.toby.service.DataProcess;
import com.toby.service.DataProcessImpl;
import com.toby.service.EmptyFilter;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * @author xiaoxl
 * @date 2022/6/15 16:29
 */
public class Main {
    private final static String FILE = "StringUtils.java";

    public static void main(String[] args) {
        DataLoader dataLoader = new DataLoaderFromFS(FILE);
        DataProcess dataProcess = new DataProcessImpl(dataLoader);
        // 添加过滤算子
        dataProcess.add(new EmptyFilter())
                .add(new CommentFilter());
        // 处理数据
        Stream<String> result = dataProcess.process();
        // 保存结果
        DataStorer dataStorer = new DataStorerToFS("validLineCount.txt");
        long count = result.count();
        System.out.println("filter result is "+ count);
        dataStorer.wirte(String.valueOf(count).getBytes(StandardCharsets.UTF_8));
    }
}
