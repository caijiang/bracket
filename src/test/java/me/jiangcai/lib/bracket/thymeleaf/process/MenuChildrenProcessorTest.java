package me.jiangcai.lib.bracket.thymeleaf.process;

import me.jiangcai.lib.bracket.TestConfig;
import org.junit.Test;
import org.luffy.test.SpringWebTest;
import org.openqa.selenium.By;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class MenuChildrenProcessorTest extends SpringWebTest{

    @Test
    public void go() throws Exception {
//        mockMvc.perform(get("/testActive"))
//                .andDo(print());

        driver.get("http://localhost/testActive");

        assertThat(driver.findElement(By.cssSelector("#testMenuParent ul")).getAttribute("style"))
                .isEqualTo("display: block;");

        assertThat(driver.findElement(By.cssSelector("#test2MenuParent ul")).getAttribute("style"))
                .isNull();

        assertThat(driver.findElement(By.cssSelector("#test3MenuParent ul")).getAttribute("style"))
                .isEqualTo("display: block;");

    }
}