package com.toby;

import com.toby.dao.DataLoader;
import com.toby.dao.DataLoaderFromFS;
import com.toby.dao.DataStorer;
import com.toby.dao.DataStorerToFS;
import com.toby.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaoxl
 * @date 2022/6/15 16:29
 */
public class Main {
    private final static String FILE = "StringUtils.java";

    public static void main(String[] args) {
        DataLoader dataLoader = new DataLoaderFromFS(FILE);
        Stream<String> stream = dataLoader.read();
        // 处理空行
        List<String> noEmptyLines = stream.filter(StringUtils::isNotBlank).collect(Collectors.toList());
        long total = noEmptyLines.size();
        // 处理注释
        long commentNum = getCommentNum(noEmptyLines);
        // 保存结果
        DataStorer dataStorer = new DataStorerToFS("validLineCount.txt");
        long count = total - commentNum;
        System.out.println("filter result is "+ count);
        dataStorer.wirte(String.valueOf(count).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 注释处理逻辑
     * @param noEmptyLines
     * @return
     */
    private static long getCommentNum(List<String> noEmptyLines) {
        long count = 0L;
        for (int i = 0; i < noEmptyLines.size(); i++) {
            String str = noEmptyLines.get(i).trim();
            if (str.startsWith("//")){
                count++;
            }else if (str.startsWith("/*") && str.endsWith("*/")){
                count++;
            }else if (str.startsWith("/*")){
                int j = i+1;
                while (j < noEmptyLines.size()){
                    String temp = noEmptyLines.get(j).trim();
                    if (temp.endsWith("*/")){
                        break;
                    }
                    j++;
                }
                count += (j-i+1);
                i = j;
            }
        }
        return count;
    }
}
