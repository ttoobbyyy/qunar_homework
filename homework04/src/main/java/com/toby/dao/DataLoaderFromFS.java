package com.toby.dao;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.stream.Stream;

/**
 * 从文件系统获取数据
 *
 * @author xiaoxl
 * @date 2022/6/14 17:39
 */
@Slf4j
public class DataLoaderFromFS implements DataLoader {
    /**
     * 文件路径
     */
    private final String path;

    public DataLoaderFromFS(String path) {
        this.path = path;
    }

    @Override
    public Stream<String> read() {
        File file = null;
        Stream<String> result = null;
        String absolutePath = System.getProperty("user.dir") +"\\"+ path;
        try {
            file = new File(absolutePath);
            result = Files.asCharSource(file, Charsets.UTF_8).lines();
        } catch (Exception e) {
            log.error("don't exists file " + absolutePath, e.getMessage());
        }
        return result;
    }
}
