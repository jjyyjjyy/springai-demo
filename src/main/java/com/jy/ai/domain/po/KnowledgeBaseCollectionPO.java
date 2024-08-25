package com.jy.ai.domain.po;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jy.ai.typehandler.JSONObjectTypeHandler;
import lombok.Data;

/**
 * @author jy
 */
@Data
@TableName("langchain_pg_collection")
public class KnowledgeBaseCollectionPO {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String name;

    @TableField(typeHandler = JSONObjectTypeHandler.class)
    private JSONObject cmetadata;
}
