package com.toby.service;

import com.google.common.base.Splitter;
import com.toby.dao.DataLoader;
import com.toby.dao.DataStorer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoxl
 * @date 2022/6/16 10:26
 */
@Slf4j
public class DataProcessImpl implements DataProcess {
    private final static String NATURE_ORDER = "natureOrder";
    private final static String INDEX_ORDER = "indexOrder";
    private final static String CHAR_ORDER = "charOrder";
    private final static String CHAR_ORDER_DESC = "charOrderDESC";
    // 暂存的读入数据
    private final List<String> props;
    private final List<String> template;
    // 替换后的结果
    private final List<String> replaceResult;
    // 四种顺序的索引
    private final String[] natureOrder;
    private final String[] indexOrder;
    private final String[] charOrder;
    private final String[] charOrderDESC;
    private final DataStorer dataStorer;

    public DataProcessImpl(DataLoader propDataLoader, DataLoader templateDataLoader, DataStorer dataStorer) {
        this.props = propDataLoader.read().collect(Collectors.toList());
        this.template = templateDataLoader.read().collect(Collectors.toList());
        this.replaceResult = new ArrayList<>(this.template.size());
        this.natureOrder = new String[props.size()];
        this.indexOrder = new String[props.size()];
        this.charOrder = new String[props.size()];
        this.charOrderDESC = new String[props.size()];
        this.dataStorer = dataStorer;
    }

    @Override
    public void process() {
        // 处理四种排序
        initOrders();

        // 替换数据
        replaceData();

        // 保存数据
        saveData();
    }

    /**
     * 保存数据
     */
    private void saveData() {
        for (String text : replaceResult){
            dataStorer.wirte(text+"\n");
        }
        dataStorer.close();
    }

    /**
     * 用排序后的数据替换文本，数据处理是基于替换字符串在同一行的情景，没有跨行场景
     */
    private void replaceData() {
        for (String text : template) {
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == '$') {
                    int j = i + 1;
                    int leftBracket = 0;
                    int rightBracket = 0;
                    for (; j < text.length(); j++) {
                        if (text.charAt(j) == '(') {
                            leftBracket = j;
                        } else if (text.charAt(j) == ')') {
                            rightBracket = j;
                            break;
                        }
                    }
                    int replaceEnd = rightBracket+1;
                    String orderType = text.substring(i+1, leftBracket);
                    String replace = text.substring(i,replaceEnd);
                    int index = Integer.parseInt(text.substring(leftBracket+1,rightBracket));
                    switch (orderType){
                        case NATURE_ORDER:
                            text = text.replace(replace,natureOrder[index]);
                            break;
                        case INDEX_ORDER:
                            text = text.replace(replace,indexOrder[index]);
                            break;
                        case CHAR_ORDER:
                            text = text.replace(replace,charOrder[index]);
                            break;
                        case CHAR_ORDER_DESC:
                            text = text.replace(replace,charOrderDESC[index]);
                            break;
                        default:
                            log.error("don't support order type: "+orderType);
                    }
                    i = j;
                }
            }
            replaceResult.add(text);
        }
    }

    /**
     * 获得四种排序结果
     */
    private void initOrders() {
        int index = 0;
        for (String prop : props) {
            List<String> split = Splitter.on("\t").omitEmptyStrings().trimResults().splitToList(prop);
            int tempIndex = Integer.parseInt(split.get(0));
            String word = split.get(1);
            natureOrder[index++] = word;
            indexOrder[tempIndex] = word;
        }
        System.arraycopy(natureOrder, 0, charOrder, 0, natureOrder.length);
        Arrays.sort(charOrder);
        for (int i = 0; i < charOrder.length; i++) {
            charOrderDESC[i] = charOrder[charOrder.length - i - 1];
        }
    }
}
