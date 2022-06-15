package com.toby.util;

/**
 * @author xiaoxl
 * @date 2022/6/15 16:12
 */
public class StringUtils {
    public static boolean isNotBlank(String str){
        return str != null && !str.trim().equals("");
    }
}
