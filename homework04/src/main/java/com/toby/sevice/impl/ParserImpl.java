package com.toby.sevice.impl;

import com.toby.entity.Cmd;
import com.toby.sevice.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author xiaoxl
 * @date 2022/6/16 18:00
 */
public class ParserImpl implements Parser {

    @Override
    public List<Cmd> parse(String cmd) {
        cmd = cmd.trim();
        if (cmd.length() == 0){
            return null;
        }
        List<Cmd> result = new ArrayList<>();
        String[] split = cmd.split("\\|");
        for (String str : split){
            str = str.trim();
            Cmd temp = new Cmd();
            int index = 0;
            while (index < str.length()){
                int endIndex = index;
                while (endIndex < str.length() && str.charAt(endIndex) != ' '){
                    endIndex++;
                }
                String type = str.substring(index,endIndex);
                if (temp.getName() == null){
                    temp.setName(type);
                }else if (type.startsWith("-")){
                    if (temp.getParams() == null){
                        temp.setParams(new ArrayList<>());
                    }
                    List<String> params = temp.getParams();
                    for (int i = 1; i < type.length(); i++) {
                        params.add(String.valueOf(type.charAt(i)));
                    }
                    temp.setParams(params);
                }else {
                    temp.setOtherParams(type);
                }
                index = endIndex+1;
            }
            result.add(temp);
        }
        return result;
    }
}
