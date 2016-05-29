package me.jiangcai.lib.bracket.thymeleaf.process;

import me.jiangcai.lib.bracket.TestConfig;
import org.junit.Test;
import org.luffy.test.SpringWebTest;
import org.openqa.selenium.By;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class ActiveMenuProcessorTest extends SpringWebTest {

    @Test
    public void go() throws Exception {
//        mockMvc.perform(get("/testActive"))
//                .andDo(print());

        driver.get("http://localhost/testActive");

        assertThat(driver.findElement(By.id("testMenu")).getAttribute("class"))
                .isEqualTo("active");

        assertThat(driver.findElement(By.id("test2Menu")).getAttribute("class"))
                .isNull();

        assertThat(driver.findElement(By.id("test3Menu")).getAttribute("class"))
                .isEqualTo("active");

        /////

        assertThat(driver.findElement(By.id("testMenuParent")).getAttribute("class"))
                .isEqualTo("na-parent nav-active active");

        assertThat(driver.findElement(By.id("test2MenuParent")).getAttribute("class"))
                .isEqualTo("na-parent");

        assertThat(driver.findElement(By.id("test3MenuParent")).getAttribute("class"))
                .isEqualTo("na-parent nav-active active");

    }

}