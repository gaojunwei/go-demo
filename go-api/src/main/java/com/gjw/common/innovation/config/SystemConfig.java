package com.gjw.common.innovation.config;

import com.gjw.common.innovation.service.UserDetailsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.HashSet;
import java.util.Set;

/**
 * @author gaojunwei
 * @date 2019/10/14 18:34
 */
@Configuration
public class SystemConfig {
    /**
     * 跨域白名单配置
     *
     * @return
     */
    @Bean("allowedOrigins")
    public Set<String> initOriginsSet() {
        Set<String> allowedOrigins = new HashSet<>();
        allowedOrigins.add("http://plc.jd.com");
        allowedOrigins.add("https://plc.jd.com");

        allowedOrigins.add("http://nbr.jd.com");
        allowedOrigins.add("https://nbr.jd.com");

        allowedOrigins.add("http://wu.jd.com");
        allowedOrigins.add("https://wu.jd.com");

        allowedOrigins.add("http://test.jdr.jd.com");
        allowedOrigins.add("https://test.jdr.jd.com");

        allowedOrigins.add("http://pb.jd.com");
        allowedOrigins.add("https://pb.jd.com");

        return allowedOrigins;
    }

    @Bean
    public UserDetailsRepository userDetailsRepository() {
        UserDetailsRepository userDetailsRepository = new UserDetailsRepository();
        // 为了让我们的登录能够运行 这里我们初始化一个用户Felordcn 密码采用明文 当你在密码12345上使用了前缀{noop} 意味着你的密码不使用加密，
        // authorities 一定不能为空 这代表用户的角色权限集合
        UserDetails felordcn = User.withUsername("admin").password("{noop}12345").authorities(AuthorityUtils.NO_AUTHORITIES).build();
        userDetailsRepository.createUser(felordcn);
        return userDetailsRepository;
    }

    @Bean
    public UserDetailsManager userDetailsManager(UserDetailsRepository userDetailsRepository) {
        return new UserDetailsManager() {
            @Override
            public void createUser(UserDetails user) {
                userDetailsRepository.createUser(user);
            }

            @Override
            public void updateUser(UserDetails user) {
                userDetailsRepository.updateUser(user);
            }

            @Override
            public void deleteUser(String username) {
                userDetailsRepository.deleteUser(username);
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                userDetailsRepository.changePassword(oldPassword, newPassword);
            }

            @Override
            public boolean userExists(String username) {
                return userDetailsRepository.userExists(username);
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userDetailsRepository.loadUserByUsername(username);
            }
        };
    }
}