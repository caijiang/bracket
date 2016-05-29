package me.jiangcai.lib.bracket.thymeleaf.process;

import org.springframework.stereotype.Component;

/**
 * 检测当前的activeMenu是否在指定参数中,如果是则调整这个标签的css使之符合bracket的样式
 *
 * @author Jolene
 */
@Component
public class ActiveMenuProcessor extends ClassesByActiveMenuProcessor {

    public ActiveMenuProcessor() {
        super("activeMenu");
    }

}
