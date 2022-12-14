package com.hao.config;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean(name = "authenticator")
    public MyModularRealmAuthenticator modularRealmAuthenticator(){
        MyModularRealmAuthenticator myDiyModularRealmAuthenticator = new MyModularRealmAuthenticator();
        myDiyModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return myDiyModularRealmAuthenticator;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(1024);
        return matcher;
    }

    @Bean
    public Realm AdminRealm(@Qualifier("credentialsMatcher") CredentialsMatcher credentialsMatcher){
        AdminRealm realm = new AdminRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        return new AdminRealm();
    }

    @Bean
    public Realm UserRealm(@Qualifier("credentialsMatcher") CredentialsMatcher credentialsMatcher) {
        UserRealm realm = new UserRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        return new UserRealm();
    }

    @Bean
    public WeChatSessionManager sessionManager() {
        WeChatSessionManager weChatSessionManager = new WeChatSessionManager();
        return weChatSessionManager;
    }

    @Bean
    public SecurityManager securityManager(@Qualifier("UserRealm") Realm Userrealm,
                                           @Qualifier("AdminRealm")Realm admin,
                                           @Qualifier("sessionManager") SessionManager sessionManager,
                                           @Qualifier("authenticator") MyModularRealmAuthenticator authenticator) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(authenticator);
        Collection<Realm> realms = new ArrayList<>();
        realms.add(Userrealm);
        realms.add(admin);
        securityManager.setRealms(realms);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.getFilters().put("authc", new ShiroHeaderFilter());



        // ????????????????????? url????????????????????????????????????
        shiroFilterFactoryBean.setLoginUrl("/");
        // ?????????????????????????????????????????? url
//        shiroFilterFactoryBean.setSuccessUrl("/admin/index");
        // ???????????????????????????????????? url
        shiroFilterFactoryBean.setUnauthorizedUrl("/");

        /*
         * anon????????????????????????
         * authc????????????????????????
         * user?????????rememberMe?????????
         * perms????????????????????????
         * roles[?????????]??????????????????????????????
         */
        //??????????????? url ?????????
        Map<String,String> filtermap = new LinkedMap();
        filtermap.put("/api/v/user","anon");
        filtermap.put("/","anon");
        filtermap.put("/index.html","anon");
        filtermap.put("/login.html","anon");
        filtermap.put("/admin/user/login","anon");

        filtermap.put("/wx/**","authc");
        filtermap.put("/wxpay/**","authc");
        filtermap.put("/api/v/my/**","authc");
        filtermap.put("/admin/**","authc");
        filtermap.put("/main.html","authc");





        shiroFilterFactoryBean.setFilterChainDefinitionMap(filtermap);


        return shiroFilterFactoryBean;
    }
}
