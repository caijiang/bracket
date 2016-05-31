package me.jiangcai.bracket.test.auth;

import me.jiangcai.lib.bracket.auth.AuthenticateService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.luffy.test.SpringWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * 其实这并不是Bracket的内容,而应该是一个独立的auth项目
 * 已认证（登录）的一个web测试,需要使用{@link LoginAs}标注配合
 *
 * @author CJ
 */
@SuppressWarnings("WeakerAccess")
@RunWith(AuthenticatedWebTest.AuthenticatedRunner.class)
public abstract class AuthenticatedWebTest<T extends UserDetails> extends SpringWebTest {

    /**
     * net版本
     * TODO 尚未实现
     */
    protected MockHttpSession session;
    private LoginAs loginType;
    protected T currentUser;
    @Autowired
    protected AuthenticateService<T> authenticateService;


    @Before
    public void authenticate() throws IllegalAccessException, InstantiationException {
        if (loginType == null) {
            throw new IllegalStateException("使用AuthenticatedWebTest作为测试基类那么需要使用@LoginAs标注声明所登录的角色.");
        }
        driver.manage().deleteAllCookies();

        currentUser = authenticateService.createUser(true, loginType.value());

        String rawPassword = UUID.randomUUID().toString();
        currentUser = authenticateService.changePassword(currentUser, rawPassword);

        driver.get("http://localhost");
        LoginPage loginPage = initPage(LoginPage.class);

        loginPage.assertLoginSuccess(currentUser.getUsername(), rawPassword);
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
