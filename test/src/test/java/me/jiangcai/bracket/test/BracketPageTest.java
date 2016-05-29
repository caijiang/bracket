package me.jiangcai.bracket.test;

import org.junit.Test;
import org.luffy.test.SpringWebTest;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author CJ
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class BracketPageTest extends SpringWebTest{

    @Test
    public void go() throws Exception {
//        mockMvc.perform(get("/index.html"))
//        .andDo(print());
        driver.get("http://localhost/index.html");

        IndexPage page = initPage(IndexPage.class);

//        System.out.println(page);

        WebElement element = page.findMenuLiByClass("fa-home");

        assertThat(element.getAttribute("class"))
        .contains("active");
    }

}