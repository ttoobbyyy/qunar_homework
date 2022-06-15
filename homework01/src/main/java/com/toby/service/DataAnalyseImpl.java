package com.toby.service;

import com.toby.dao.DataLoader;
import com.toby.dao.entity.Request;
import com.toby.util.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaoxl
 * @date 2022/6/14 23:13
 */
@Slf4j
public class DataAnalyseImpl implements DataAnalyse{
    private final DataLoader dataLoader;
    private final List<Request> preProcessData;

    public DataAnalyseImpl(DataLoader dataLoader){
        this.dataLoader = dataLoader;
        this.preProcessData = preProcess();
    }

    /**
     * 数据预处理
     * @return
     */
    private List<Request> preProcess(){
        Map<String, Pattern> patternCache = getPatternCache();
        Stream<String> stream = dataLoader.get();
        List<Request> requests = stream.map(line -> {
            Request request = new Request();
            request.setNum(1L);
            try {
                request.setPath(getRequestPath(patternCache, line));
                request.setMethod(getRequestMethod(patternCache, line));
                request.setPrePath(getRequestPrePath(patternCache, line));
                return request;
            } catch (Exception e) {
                log.error(line +" data parse error, ", e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return requests;
    }

    /**
     * 利用正则匹配获取请求路径前缀
     * @param patternCache
     * @param line
     * @return
     * @throws Exception
     */
    private String getRequestPrePath(Map<String, Pattern> patternCache, String line) throws Exception {
        Matcher matcher = patternCache.get(Constants.REQUEST_URL_PREFIX).matcher(line);
        if (matcher.find()){
            return matcher.group();
        }
        throw new Exception("prePath parse error");
    }

    /**
     * 利用正则匹配获取请求方法
     * @param patternCache
     * @param line
     * @return
     * @throws Exception
     */
    private String getRequestMethod(Map<String, Pattern> patternCache, String line) throws Exception {
        Matcher matcher = patternCache.get(Constants.REQUEST_METHOD).matcher(line);
        if (matcher.find()){
            return matcher.group();
        }
        throw new Exception("method parse error");
    }

    /**
     * 利用正则匹配获取请求的路径
     * @param patternCache
     * @param line
     * @return
     */
    private String getRequestPath(Map<String, Pattern> patternCache, String line) throws Exception {
        Pattern pattern1 = patternCache.get(Constants.REQUEST_URL_PATTERN2);
        Matcher matcher1 = pattern1.matcher(line);
        if (matcher1.find()){
            return matcher1.group();
        }

        Pattern pattern2 = patternCache.get(Constants.REQUEST_URL_PATTERN1);
        Matcher matcher2 = pattern2.matcher(line);
        if (matcher2.find()){
            return matcher2.group();
        }
        throw new Exception("path parse error");
    }

    /**
     * 获取正则匹配的缓存pattern，提高一定数据处理性能
     * @return
     */
    private Map<String, Pattern> getPatternCache() {
        Map<String, Pattern> patternMap = new HashMap<>();
        patternMap.put(Constants.REQUEST_METHOD,Pattern.compile(Constants.REQUEST_METHOD_REGEX));
        patternMap.put(Constants.REQUEST_URL_PATTERN1,Pattern.compile(Constants.REQUEST_URL_REGEX1));
        patternMap.put(Constants.REQUEST_URL_PATTERN2,Pattern.compile(Constants.REQUEST_URL_REGEX2));
        patternMap.put(Constants.REQUEST_URL_PREFIX,Pattern.compile(Constants.REQUEST_URL_PREFIX_REGEX));
        return patternMap;
    }

    @Override
    public long getTotalRequest() {
        return preProcessData.size();
    }

    @Override
    public List<Request> getTopTenRequest() {
        Map<String, Long> counter = new HashMap<>();
        for (Request preProcessDatum : preProcessData) {
            String key =
                    preProcessDatum.getMethod() + Constants.SEPARATOR + preProcessDatum.getPath() + Constants.SEPARATOR + preProcessDatum.getPrePath();
            counter.put(key, counter.getOrDefault(key, 0L) + 1);
        }

        PriorityQueue<String> order = new PriorityQueue<>((o1,o2) -> (int) (counter.get(o2)-counter.get(o1)));
        order.addAll(counter.keySet());

        List<Request> topTenRequests = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            String temp = order.poll();
            String[] splits = temp.split(Constants.SEPARATOR);
            Request request = new Request(splits[1], splits[0], counter.get(temp), splits[2], null);
            topTenRequests.add(request);
        }
        return topTenRequests;
    }

    @Override
    public long getMethodNum(String method) {
        return preProcessData.stream().filter(request -> request.getMethod().equals(method)).count();
    }

    @Override
    public List<Request> classifyByRequestPrefix() {
        Map<String, Request> result = new HashMap<>();
        for (Request request : preProcessData){
            if (!result.containsKey(request.getPrePath())){
                Request temp = new Request();
                temp.setPrePath(request.getPrePath());
                temp.setUrls(new HashSet<>());
                result.put(request.getPrePath(),temp);
            }
            Request temp = result.get(request.getPrePath());
            temp.getUrls().add(getSubPath(request));
        }
        return new ArrayList<>(result.values());
    }

    private String getSubPath(Request request) {
        return request.getPath().substring(request.getPrePath().length()-1);
    }
}
