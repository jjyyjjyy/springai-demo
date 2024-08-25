package com.jy.ai.util;

import com.alibaba.fastjson2.JSONObject;
import com.jy.ai.annotation.FieldDescription;
import org.springframework.util.ReflectionUtils;

/**
 * @author jy
 */
public class FieldUtils {

    public static JSONObject extractFieldDescription(Object object) {
        if (object == null) {
            return new JSONObject();
        }
        JSONObject jsonObject = new JSONObject();
        ReflectionUtils.doWithFields(object.getClass(),
            field -> {
                field.setAccessible(true);
                String desc = field.getAnnotation(FieldDescription.class).value();
                jsonObject.put(desc, field.get(object));
            }
            , field -> field.isAnnotationPresent(FieldDescription.class));
        return jsonObject;
    }

}
