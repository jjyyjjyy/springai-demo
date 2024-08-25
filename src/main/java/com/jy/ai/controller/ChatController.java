package com.jy.ai.controller;

import com.jy.ai.domain.param.ChatCompletionParam;
import com.jy.ai.domain.po.KnowledgeBaseCollectionPO;
import com.jy.ai.service.KnowledgeBaseCollectionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author jy
 */
@RestController
public class ChatController {

    private static final String RAG_PROMPT = "[指令] 根据已知信息，简洁和专业的来回答问题。如果无法从中得到答案，请说 “根据已知信息无法回答该问题”，不允许在答案中添加编造成分，答案请使用中文。\n" +
        "{question_answer_context}";

    @Autowired
    private ChatClient xinferenceChatClient;

    @Autowired
    private VectorStore ragVectorStore;

    @Autowired
    private KnowledgeBaseCollectionService knowledgeBaseCollectionService;

    @PostMapping(path = "/chat", produces = MediaType.TEXT_MARKDOWN_VALUE)
    public String chat(@RequestBody ChatCompletionParam param) {
        return buildChatSpec(param)
            .call()
            .content();
    }

    @PostMapping(path = "/stream-chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody ChatCompletionParam param) {
        return buildChatSpec(param)
            .stream()
            .content();
    }

    private ChatClient.ChatClientRequestSpec buildChatSpec(ChatCompletionParam param) {
        ChatClient.ChatClientRequestSpec spec = xinferenceChatClient.prompt();
        if (StringUtils.isNotBlank(param.getCollectionName())) {
            KnowledgeBaseCollectionPO collection = knowledgeBaseCollectionService.findByName(param.getCollectionName());
            if (collection == null) {
                throw new RuntimeException("知识库不存在!");
            }
            spec.advisors(new QuestionAnswerAdvisor(ragVectorStore, SearchRequest.defaults()
//                .withFilterExpression("'collection_id' == '" + collection.getUuid() + "'")
                , RAG_PROMPT));
        }
        if (StringUtils.isNotBlank(param.getFunctionName())) {
            spec.functions(param.getFunctionName());
        }
        return spec
            .messages(new UserMessage(param.getMessage()));
    }

}
