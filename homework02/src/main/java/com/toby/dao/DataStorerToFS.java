package com.toby.dao;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author xiaoxl
 * @date 2022/6/15 15:26
 */
@Slf4j
public class DataStorerToFS implements DataStorer{
    /**
     * 文件路径
     */
    private final String path;

    /**
     * 是否是绝对路径，默认是相对路径
     */
    private final Boolean isAbsolute;

    public DataStorerToFS(String path){
        this(path,false);
    }

    public DataStorerToFS(String path, Boolean isAbsolute){
        this.path = path;
        this.isAbsolute = isAbsolute;
    }

    @Override
    public void wirte(byte[] contents) {
        File file = null;
        try {
            if (isAbsolute){
                file = new File(path);
            }else {
                String absolutePath = new File("").getAbsolutePath() + "\\homework02\\src\\main\\resources\\" + path;
                file = new File(absolutePath);
            }
            file.createNewFile();
            Files.write(contents, file);
        }catch (Exception e){
            log.error("error save path "+path, e);
        }
    }
}
