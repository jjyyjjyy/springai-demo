package com.jy.ai.function;

import com.alibaba.fastjson2.JSONObject;
import com.jy.ai.annotation.FieldDescription;
import com.jy.ai.util.FieldUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SearchWeatherService implements Function<SearchWeatherService.WeatherRequest, JSONObject> {

    private static final String WEATHER_API_URL = "http://api.seniverse.com/v3/weather/now.json";

    @Autowired
    private RestTemplate restTemplate;

    private final String key;

    public SearchWeatherService(String key) {
        this.key = key;
    }

    public JSONObject apply(WeatherRequest request) {
        WeatherResponse weatherResponse = getWeatherResponse(request);
        List<WeatherResult> responseResult = weatherResponse.getResults();
        JSONObject result = new JSONObject();
        if (CollectionUtils.isEmpty(responseResult)) {
            return new JSONObject();
        }
        WeatherResult weatherResult = responseResult.getFirst();
        result.putAll(FieldUtils.extractFieldDescription(weatherResult.getLocation()));
        Weather now = weatherResult.getNow();
        Optional.ofNullable(request.getUnit()).map(WeatherUnit::getDesc).ifPresent(now::setUnit);
        result.putAll(FieldUtils.extractFieldDescription(now));
        return result;
    }

    private WeatherResponse getWeatherResponse(WeatherRequest request) {
        String apiUrl = UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
            .queryParam("key", this.key)
            .queryParam("location", request.getCity())
            .queryParam("unit", request.getUnit())
            .build(false)
            .toUriString();
        return restTemplate.getForEntity(apiUrl, WeatherResponse.class).getBody();
    }

    @AllArgsConstructor
    @Getter
    public enum WeatherUnit {
        c("摄氏度"),
        f("华氏度");
        private final String desc;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class WeatherRequest {
        private String city;
        private WeatherUnit unit = WeatherUnit.c;

        public WeatherRequest(String city) {
            this.city = city;
        }

    }

    @Data
    public static class WeatherResponse {
        private List<WeatherResult> results;
    }

    @Data
    public static class WeatherResult {
        private WeatherLocation location;
        private Weather now;
        private OffsetDateTime last_update;
    }

    @Data
    public static class WeatherLocation {
        private String id;
        @FieldDescription("城市")
        private String name;
        private String country;
        private String path;
        private String timezone;
        private String timezone_offset;
    }

    @Data
    public static class Weather {
        @FieldDescription("天气现象")
        private String text;
        private String code;
        @FieldDescription("温度")
        private String temperature;
        @FieldDescription("温度单位")
        private String unit;
        @FieldDescription("体感温度")
        private String feels_like;
        @FieldDescription("气压 (单位为mb百帕)")
        private String pressure;
        @FieldDescription("相对湿度 (单位为%)")
        private String humidity;
        @FieldDescription("能见度 (单位为km)")
        private String visibility;
        @FieldDescription("风向")
        private String wind_direction;
        @FieldDescription("风向角度 (范围0~360, 0为正北, 90为正东, 180为正南, 270为正西)")
        private String wind_direction_degree;
        @FieldDescription("风速")
        private String wind_speed;
        @FieldDescription("风力等级")
        private String wind_scale;
        @FieldDescription("云量 (单位为%)")
        private String clouds;
        @FieldDescription("露点温度")
        private String dew_point;
    }
}
