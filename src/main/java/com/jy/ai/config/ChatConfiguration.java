package com.jy.ai.config;

import com.alibaba.fastjson2.JSONObject;
import com.jy.ai.function.SearchWeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

/**
 * @author jy
 */
@Configuration
public class ChatConfiguration {

    @Bean
    public ChatClient xinferenceChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean("SearchWeather")
    @Description("查询指定地区的温度")
    @ConditionalOnProperty(name = "tools.searchWeather.key")
    public Function<SearchWeatherService.WeatherRequest, JSONObject> searchWeatherFunction(@Value("${tools.searchWeather.key}") String key) {
        return new SearchWeatherService(key);
    }
}
