package me.jiangcai.bracket.test;

import me.jiangcai.lib.test.page.AbstractPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Bracket页面的一些易用功能
 *
 * @author CJ
 */
@SuppressWarnings("WeakerAccess")
public abstract class BracketPage extends AbstractPage {

    private static final Log log = LogFactory.getLog(BracketPage.class);

    public BracketPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public AbstractBracketPageAssert<?, ? extends BracketPage> assertMe() {
        return AbstractBracketPageAssert.assertThat(this);
    }

    /**
     * 关闭所有gritter 的danger消息
     *
     * @throws InterruptedException 这个操作需时可能会被其他线程打断
     * @since 1.1
     */
    public void closeGritterDangeMessages() throws InterruptedException {
        closeGritterMessage("growl-danger");
    }

    private void closeGritterMessage(String typeClass) throws InterruptedException {
        while (true) {
            List<WebElement> elements = webDriver.findElements(By.className(typeClass));
            if (elements.isEmpty())
                break;
            for (WebElement message : elements) {
                WebElement close = message.findElement(By.className("gritter-close"));
                if (close.isDisplayed())
                    close.click();
            }
            Thread.sleep(100);
        }

    }

    //    @NotNull
    protected List<String> getGritterMessage(String typeClass) {
        List<WebElement> elements = webDriver.findElements(By.className(typeClass));
        List<String> messages = new ArrayList<>();
        if (!elements.isEmpty()) {
            elements.forEach(type -> type.findElements(By.className("gritter-item")).forEach(item -> {
                String message = item.findElement(By.tagName("p")).getText();
                messages.add(message);
            }));
        }
        return messages;
    }


    /**
     * 点击这个class的菜单
     *
     * @param className className
     * @since 1.1
     */
    public void clickMenuByClass(String className) {
        findMenuLiByClass(className, this::clickElement);
    }

    /**
     * @param element 点击这个东西
     */
    private void clickElement(WebElement element) {
        try {
            element.findElement(By.tagName("a")).click();
        } catch (NoSuchElementException ignored) {
            //noinspection EmptyCatchBlock
            try {
                element.findElement(By.tagName("button")).click();
            } catch (NoSuchElementException ignored1) {
                //noinspection EmptyCatchBlock
                try {
                    element.findElement(By.tagName("img")).click();
                } catch (NoSuchElementException ignored2) {
                    log.warn("找不到里面的可点击目标,将直接点击自身", ignored2);
                    printThisPage();
                    element.click();
                }
            }
        }
    }

    /**
     * 根据className定位菜单li,再将它消耗掉
     *
     * @param className 比如fa-home
     * @param consumer  消耗者
     * @since 1.1
     */
    public void findMenuLiByClass(String className, Consumer<WebElement> consumer) {
        WebElement element = findMenuLiByClass(className);
        consumer.accept(element);
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
        for (WebElement element : ulList) {
            if (element.getAttribute("class").contains("mb30"))
                continue;
            if (element.getAttribute("class").contains("nav-justified"))
                continue;
            ul = element;
        }

        if (ul == null)
            throw new IllegalStateException("找不到必要的导航ul nav");

        List<WebElement> list = ul.findElements(By.tagName("li"));

        WebElement parentElement = null;
        for (WebElement element : list) {
            if (element.findElements(By.className(className)).isEmpty())
                continue;

            // 排除上级菜单
            if (element.getAttribute("class") != null && element.getAttribute("class").contains("nav-parent")) {
                parentElement = element;
                continue;
            }

            if (element.isDisplayed()) {
                return element;
            } else {
                if (parentElement == null)
                    throw new IllegalStateException("指定的菜单无法展示" + className);
                parentElement.click();
                return element;
            }
        }
        throw new AssertionError("找不到" + className);
    }
}
