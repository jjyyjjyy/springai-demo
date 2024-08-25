package com.jy.ai.annotation;

import java.lang.annotation.*;

/**
 * @author jy
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FieldDescription {

    String value();
}
