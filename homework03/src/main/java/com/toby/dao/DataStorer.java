package com.toby.dao;

/**
 * @author xiaoxl
 * @date 2022/6/15 15:12
 */
public interface DataStorer {
    /**
     * 将内容保存
     * @param contents
     */
    void wirte(String content);

    /**
     * 关闭流，释放资源
     */
    void close();
}
