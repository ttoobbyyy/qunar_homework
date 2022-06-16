package com.toby.dao;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author xiaoxl
 * @date 2022/6/15 15:26
 */
@Slf4j
public class DataStorerToFS implements DataStorer{
    /**
     * 文件路径
     */
    private final OutputStreamWriter writer;

    public DataStorerToFS(String path){
        writer = initWriter(path);
    }

    /**
     * 初始化写入流
     * @param path
     * @return
     */
    private OutputStreamWriter initWriter(String path) {
        OutputStreamWriter writer = null;
        try {
            String absolutePath = new File("").getAbsolutePath() + "\\homework03\\src\\main\\resources\\" + path;
            System.out.println(absolutePath);
            File file = new File(absolutePath);
            file.createNewFile();
            writer = new OutputStreamWriter(new FileOutputStream(file,true), StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error("error save path "+ path, e);
        }
        return writer;
    }

    @Override
    public void wirte(String content) {
        try {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(){
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
