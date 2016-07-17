package me.jiangcai.bracket.test.auth;

import me.jiangcai.lib.bracket.auth.AuthenticateService;
import me.jiangcai.lib.test.page.AbstractPage;
import me.jiangcai.lib.test.page.AbstractPageAssert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * 其实这并不是Bracket的内容,而应该是一个独立的auth项目
 * 已认证（登录）的一个web测试,需要使用{@link LoginAs}标注配合
 *
 * @author CJ
 */
@SuppressWarnings("WeakerAccess")
@RunWith(AuthenticatedWebTest.AuthenticatedRunner.class)
public abstract class AuthenticatedWebTest<T extends UserDetails> extends me.jiangcai.lib.test.SpringWebTest {

    private static final Log log = LogFactory.getLog(AuthenticatedWebTest.class);

    /**
     * net版本
     */
    protected MockHttpSession session;
    protected T currentUser;
    @Autowired
    protected AuthenticateService<T> authenticateService;
    private LoginAs loginType;
    private String loginPageUri;

    /**
     * 断言登录页面
     *
     * @param pageAssert 页面断言
     * @since 1.1
     */
    protected void assertLoginPage(AbstractPageAssert<?, ? extends AbstractPage> pageAssert) {

    }

    @Before
    public void authenticate() throws Exception {
        if (loginType == null) {
            log.warn("can not authenticate without @LoginAs");
            return;
//            throw new IllegalStateException("使用AuthenticatedWebTest作为测试基类那么需要使用@LoginAs标注声明所登录的角色.");
        }
        driver.manage().deleteAllCookies();

        currentUser = authenticateService.createUser(true, loginType.value());

        String rawPassword = UUID.randomUUID().toString();
        currentUser = authenticateService.changePassword(currentUser, rawPassword);

        driver.get("http://localhost" + loginType.indexUri());
        LoginPage loginPage = initPage(LoginPage.class);

        AbstractPageAssert<?, ? extends AbstractPage> abstractPageAssert = loginPage.assertMe();
        assertLoginPage(abstractPageAssert);

        loginPage.assertLoginSuccess(currentUser.getUsername(), rawPassword);

        // MVC
        loginPageUri = "";
        MvcResult indexResult = mockMvc.perform(get(""))
                .andReturn();

        session = (MockHttpSession) indexResult.getRequest().getSession(true);

        Document document = documentFrom(indexResult);
        Element loginForm = findLoginForm(document.getElementsByTag("form"));

        Element username = findUsernameInput(loginForm);
        Element password = findPasswordInput(loginForm);

        String actionUri;
        String actionValue = loginForm.attr("action");
        if (!actionValue.startsWith("/")) {
            int lastIndex = loginPageUri.lastIndexOf("/");
            if (lastIndex == -1)
                actionUri = actionValue;
            else {
                actionUri = loginPageUri.substring(0, lastIndex + 1) + actionValue;
            }
        } else
            actionUri = actionValue;

        mockMvc.perform(post(actionUri).session(session)
                .param(username.attr("name"), currentUser.getUsername())
                .param(password.attr("name"), rawPassword)
        );
    }


    private Element findPasswordInput(Element form) {
        for (Element input : form.getElementsByTag("input")) {
            if (input.attr("name").contains("pass"))
                return input;
            if (input.attr("name").contains("pswd"))
                return input;
        }
        throw new IllegalStateException("can not find password input");
    }

    private Element findUsernameInput(Element form) {
        for (Element input : form.getElementsByTag("input")) {
            if (input.attr("name").contains("name"))
                return input;
        }
        throw new IllegalStateException("can not find username input");
    }

    private Element findLoginForm(Elements forms) {
        if (forms.size() == 1)
            return forms.get(0);
        for (Element form : forms) {
            String s1 = form.attr("name");
            String s2 = form.attr("id");
            String s3 = form.attr("action");
            if (s1 != null && s1.contains("login"))
                return form;
            if (s2 != null && s2.contains("login"))
                return form;
            if (s3 != null && s3.contains("login"))
                return form;
            if (s1 != null && s1.contains("auth"))
                return form;
            if (s2 != null && s2.contains("auth"))
                return form;
            if (s3 != null && s3.contains("auth"))
                return form;
        }
        throw new IllegalStateException("can not find LoginForm");
    }

    private Document documentFrom(MvcResult result) throws Exception {
        if (result.getResponse().getStatus() == 200) {
            return Jsoup.parse(result.getResponse().getContentAsString());
        }
        if (result.getResponse().getStatus() == 302) {
            loginPageUri = result.getResponse().encodeRedirectURL(result
                    .getResponse().getRedirectedUrl());
            return documentFrom(mockMvc.perform(get(loginPageUri).session(session)).andReturn());
        }
        throw new IllegalStateException("" + result.getResponse().getStatus());
    }


    public static class AuthenticatedRunner extends SpringJUnit4ClassRunner {

        public AuthenticatedRunner(Class<?> clazz) throws InitializationError {
            super(clazz);
        }

        @Override
        protected Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
            if (testInstance instanceof AuthenticatedWebTest) {
                // 抓取LoginAs
                LoginAs defaultLogin = testInstance.getClass().getAnnotation(LoginAs.class);
                LoginAs methodLogin = frameworkMethod.getAnnotation(LoginAs.class);
                ((AuthenticatedWebTest) testInstance).loginType = methodLogin != null ? methodLogin : defaultLogin;
            }
            return super.withBefores(frameworkMethod, testInstance, statement);
        }
    }
}
