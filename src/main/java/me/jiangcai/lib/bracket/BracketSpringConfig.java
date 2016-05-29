package me.jiangcai.lib.bracket;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 使用该模块需要载入该Spring配置
 * @author CJ
 */
@Configuration
@ComponentScan(
        {"me.jiangcai.lib.bracket.thymeleaf"}
)
public class BracketSpringConfig {
}
