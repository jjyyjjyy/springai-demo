package com.jy.ai.domain.param;

import lombok.Data;

/**
 * @author jy
 */
@Data
public class ChatCompletionParam {

    /**
     * 问题
     */
    private String message;

    /**
     * 知识库名称
     */
    private String collectionName;

    /**
     * 函数名称
     */
    private String functionName;

}
