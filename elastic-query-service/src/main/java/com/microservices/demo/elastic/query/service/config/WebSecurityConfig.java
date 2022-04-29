package com.microservices.demo.elastic.query.service.config;

import com.microservices.demo.config.UserAuthConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthConfig userAuthConfig;

    public WebSecurityConfig(UserAuthConfig userAuthConfig) {
        this.userAuthConfig = userAuthConfig;
    }
    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                //.authorizeRequests().antMatchers("/**").permitAll();//Permits all requests without authorization
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .hasRole("USER")
                .and()
                .csrf().disable();// csrf should be enabled with Browser interaction


    }
    @Override
    public  void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser(userAuthConfig.getUserName())
                .password(passwordEncoder().encode(userAuthConfig.getPassword()))
                .roles(userAuthConfig.getRoles());

    }

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();


    }

}
