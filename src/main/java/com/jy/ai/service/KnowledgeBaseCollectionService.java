package com.jy.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jy.ai.domain.po.KnowledgeBaseCollectionPO;
import com.jy.ai.mapper.KnowledgeBaseCollectionMapper;
import org.springframework.stereotype.Service;

/**
 * @author jy
 */
@Service
public class KnowledgeBaseCollectionService extends ServiceImpl<KnowledgeBaseCollectionMapper, KnowledgeBaseCollectionPO> {

    public KnowledgeBaseCollectionPO findByName(String name) {
        return this.getOne(new LambdaQueryWrapper<KnowledgeBaseCollectionPO>().eq(KnowledgeBaseCollectionPO::getName, name));
    }

}
