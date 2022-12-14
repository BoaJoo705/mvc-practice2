package org.example.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})   //클래스에 @을 붙일수 있음
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String value() default "";

    String path() default "";
}
