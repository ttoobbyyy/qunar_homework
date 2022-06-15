package com.toby;

import com.toby.dao.DataLoader;
import com.toby.dao.DataLoaderFromFS;
import com.toby.dao.entity.Request;
import com.toby.service.DataAnalyse;
import com.toby.service.DataAnalyseImpl;

import java.util.List;

/**
 * @author xiaoxl
 * @date 2022/6/15 13:47
 */
public class Main {
    private final static String FILE = "access.log";
    private final static String GET_METHOD = "GET";
    private final static String POST_METHOD = "POST";

    public static void main(String[] args) {
        // 加载数据
        DataLoader dataLoader = new DataLoaderFromFS(FILE);
        // 数据处理
        DataAnalyse dataAnalyse = new DataAnalyseImpl(dataLoader);

        // 第一问
        System.out.println(FILE + " file total is "+dataAnalyse.getTotalRequest()+" requests! ");
        System.out.println("============================================================================");

        // 第二问
        List<Request> topTenRequest = dataAnalyse.getTopTenRequest();
        for (int i = 0; i < topTenRequest.size(); i++) {
            System.out.println("the "+(i+1)+" request infos: ");
            System.out.println("request method \t\t"+topTenRequest.get(i).getMethod());
            System.out.println("request path \t\t"+topTenRequest.get(i).getPath());
            System.out.println("request num \t\t"+topTenRequest.get(i).getNum());
        }
        System.out.println("============================================================================");

        // 第三问
        System.out.println(GET_METHOD+" method total is "+dataAnalyse.getMethodNum(GET_METHOD));
        System.out.println(POST_METHOD+" method total is "+dataAnalyse.getMethodNum(POST_METHOD));
        System.out.println("============================================================================");

        // 第四问
        List<Request> requests = dataAnalyse.classifyByRequestPrefix();
        for (Request request : requests){
            System.out.println(request.getPrePath()+"====>"+request.getUrls());
        }
    }
}
