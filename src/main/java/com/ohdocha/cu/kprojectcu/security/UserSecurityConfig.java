package com.ohdocha.cu.kprojectcu.security;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
public class UserSecurityConfig {


    @Configuration
    @Order(0)
    public static class UserConfiguration extends WebSecurityConfigurerAdapter {


        private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

        @Autowired
        private DataSource dataSource;

        @Autowired
        public AuthenticationFailureHandler authenticationFailureHandler;

        @Autowired
        public DochaLoginSuccessHandler dochaLoginSuccessHandler;

        @Autowired
        public DochaAccessDeniedHandler dochaAccessDeniedHandler;

        @Autowired
        public DochaAuthenticationEntryPoint dochaAuthenticationEntryPoint;

        @Autowired
        public DochaAuthenticationProvider dochaAuthenticationProvider;

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests()
                    .antMatchers("/user/js/**").permitAll()
                    .antMatchers("/user/img/**").permitAll()
                    .antMatchers("/csdeal_img/**").permitAll()
                    .antMatchers("/car_images/**").permitAll()
                    .antMatchers("/appcache.manifest").permitAll()
                    .antMatchers("/robots.txt").permitAll()
                    .antMatchers("/login/google").permitAll()
                    .antMatchers("/user/login/**").permitAll()
                    .antMatchers("/user/main.do").permitAll()
                    .antMatchers("/user/manifest.json").permitAll()
                    .antMatchers("/index.do").permitAll()
                    .antMatchers("/user/index.do").permitAll()
                    .antMatchers("/**/login").permitAll()
                    .antMatchers("/**/login.do").permitAll()
                    .antMatchers("/login.do").permitAll()
                    .antMatchers("/user/login.do").permitAll()
                    // menu
                    .antMatchers("/event.do").permitAll()
                    .antMatchers("/couponBook.do").permitAll()
                    .antMatchers("/faq.do").permitAll()
                    .antMatchers("/notice.do").permitAll()
                    .antMatchers("/question.do").permitAll()

                    .antMatchers("/**/loginFail").permitAll()
                    .antMatchers("/**/logout").permitAll()
                    .antMatchers("/user/mypage/find_id_result.do").permitAll()
                    .antMatchers("/user/certifications/confirm.do").permitAll()
                    .antMatchers("/user/social/signup/step2.do").permitAll()
                    .antMatchers("/user/social/**").permitAll()
                    .antMatchers("/user/social/main.do").permitAll()
                    .antMatchers("/user/social/getKakaoToken.json").permitAll()
                    .antMatchers("/user/signup/register.do").permitAll()
                    .antMatchers("/user/signup/**").permitAll()
                    .antMatchers("/user/signup/terms/**").permitAll()
                    .antMatchers("/user/mypage/certificationSuccess.do").permitAll()
                    .antMatchers("/user/mypage/new_password.do").permitAll()
                    .antMatchers("/user/mypage/newPasswordSave.do").permitAll()
                    .antMatchers("/user/certifications/social/confirm.do").permitAll()
                    .antMatchers("main/findAddess.do").permitAll()
                    .antMatchers("/user/getServerTime.json").permitAll()
                    .antMatchers("oauth/kakao/**").permitAll()
                    .antMatchers("/user/mypage/manifest.json").permitAll()
                    .antMatchers("/member/login.do").permitAll()
                    .antMatchers("/user/payment/**").hasAuthority("RU")
                    .antMatchers("/user/**").hasAuthority("RU")
                    .antMatchers("/rentcar/**").hasAuthority("CA")
                    .antMatchers("/user/carSearch/**").hasAuthority("RU")
                    .and()
                    .exceptionHandling()
                    //.accessDeniedPage("/admin/login.do")
                    .accessDeniedHandler(dochaAccessDeniedHandler)
                    .authenticationEntryPoint(dochaAuthenticationEntryPoint)
                    .and()
                    .csrf().disable();


            http.formLogin().loginPage("/member/login.do")
                    .loginProcessingUrl("/member/loginprocess.do")
                    .usernameParameter("userId")
                    .passwordParameter("userPassword")
                    .successHandler(dochaLoginSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                    .logout()
                    .logoutUrl("/member/logout");

            http.formLogin().loginPage("/user/login.do")
                    .loginProcessingUrl("/user/loginprocess.do")
                    .usernameParameter("userId")
                    .passwordParameter("userPassword")
                    .successHandler(dochaLoginSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                    .logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessHandler(new DochaLogoutSuccessHandler());

            http.headers().frameOptions().disable();

            http.headers().defaultsDisabled().contentTypeOptions();
            http.headers().frameOptions().sameOrigin();
            http.headers().xssProtection().block(false);

        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/resources/**", "/static/**", "/robots.txt", "/favicon.ico", "/appcache.manifest", "/csdeal_img/**", "/car_images/**");
        }

        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        /*
         * REMEMBER-ME
         *
         */
        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
            JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
            db.setDataSource(dataSource);
            return db;
        }

        @Bean
        public DochaLogoutSuccessHandler dochaLogoutSuccessHandler() {

            return new DochaLogoutSuccessHandler();
        }

    }//end UserConfiguration


}