package me.jiangcai.lib.bracket.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CJ
 */
public interface AuthenticateService<T extends UserDetails> extends UserDetailsService {

    /**
     * 修改并保存密码
     * @param user 用户
     * @param rawPassword 明文密码
     * @return 已保存密码的用户
     */
    @Transactional
    T changePassword(T user,String rawPassword);

    /**
     * 创建新的有效用户
     * @param random 是否建立随机数据
     * @param roles 角色
     * @return 新建用户
     */
    @Transactional
    T createUser(boolean random,String... roles);

}
