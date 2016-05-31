package me.jiangcai.bracket.test.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 以该身份登录
 *
 * @author Jolene
 */
@SuppressWarnings("WeakerAccess")
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LoginAs {

    /**
     * @return role names
     */
    String[] value();

}
