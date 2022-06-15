package com.toby.dao;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.stream.Stream;

/**
 * 从文件系统获取数据
 * @author xiaoxl
 * @date 2022/6/14 17:39
 */
@Slf4j
public class DataLoaderFromFS implements DataLoader{
    /**
     * 文件路径
     */
    private final String path;

    /**
     * 是否是绝对路径，默认是相对路径
     */
    private final Boolean isAbsolute;

    public DataLoaderFromFS(String path){
        this(path,false);
    }

    public DataLoaderFromFS(String path, Boolean isAbsolute){
        this.path = path;
        this.isAbsolute = isAbsolute;
    }

    @Override
    public Stream<String> get() {
        File file = null;
        Stream<String> result = null;
        try {
            if (isAbsolute){
                file = new File(path);
            }else {
                file = new File(DataLoaderFromFS.class.getClassLoader().getResource(path).getPath());
            }
            result = Files.asCharSource(file, Charsets.UTF_8).lines();
        }catch (Exception e){
            log.error("read path "+path, e);
        }
        return result;
    }
}
