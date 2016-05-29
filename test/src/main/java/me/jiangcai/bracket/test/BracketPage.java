package me.jiangcai.bracket.test;

import org.luffy.test.page.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Bracket页面的一些易用功能
 *
 * @author CJ
 */
public abstract class BracketPage extends AbstractPage {

    public BracketPage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * 根据className定位菜单li
     *
     * @param className 比如fa-home
     * @return li webElement,没有找到则会返回null
     */
    public WebElement findMenuLiByClass(String className) {
        // 排除掉 mb30,nav-justified class 或者在 visible-xs内
        //
        List<WebElement> ulList = webDriver.findElements(By.cssSelector("ul[class~=nav]"));
        WebElement ul = null;
        for (WebElement element:ulList){
            if (element.getAttribute("class").contains("mb30"))
                continue;
            if (element.getAttribute("class").contains("nav-justified"))
                continue;
            ul = element;
        }

        if (ul==null)
            return null;

        List<WebElement> list = ul.findElements(By.tagName("li"));

        for (WebElement element : list) {

            if (!element.findElements(By.className(className)).isEmpty())
                return element;
        }
        return null;
    }
}
