//package com.havryliuk.config;
//
//import com.havryliuk.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
//        this.userService = userService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
//                .antMatchers("/login", "/index").permitAll()
//                .antMatchers("/trips/*").authenticated()
//                .and()
//                .formLogin().loginPage("/login").permitAll()
//                .failureUrl("/login?error=true")
//                .and().rememberMe()
//                .and()
//                .logout().logoutSuccessUrl("/login")
//                .and().csrf().disable();
//
//
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService)
//                .passwordEncoder(passwordEncoder);
//    }
//
//
//
//}
