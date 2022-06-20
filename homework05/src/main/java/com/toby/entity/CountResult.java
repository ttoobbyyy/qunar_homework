package com.toby.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: xiaoxl
 * @Date: 2022/6/19
 */
@Data
@AllArgsConstructor
public class CountResult implements Serializable {

    private static final long serialVersionUID = -8273866537657794206L;
    private Integer chineseCharacters;
    private Integer englishCharacters;
    private Integer punctuationCharacters;
}
