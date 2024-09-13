package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scm.services.Impl.SecurityCustomUserDetailService;

@Configuration

public class SecurityConfing {

    // @Bean
    // public UserDetailsService userDetailsService() {

    // UserDetails user1 =
    // User.withDefaultPasswordEncoder().username("admin123").password("admin123").roles("ADMIN","USER").build();
    // UserDetails
    // user2=User.withDefaultPasswordEncoder().username("user123").password("user123").build();
    // var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1,user2
    // );
    // return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailService uSecurityCustomUserDetailService;

    @Autowired
    private OAuthAutheticationSuccesHandler handler;

    @Autowired
    private AouthfailureHandler aouthfailureHandler;

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(uSecurityCustomUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;

    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // configuration

        // public url's configuration
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    // authorize.requestMatchers("/home").permitAll();
                    authorize.requestMatchers("/user/**").authenticated();
                    authorize.requestMatchers("/api/**").authenticated();
                    authorize.anyRequest().permitAll();
                }).formLogin(login -> {
                    login.loginPage("/login")
                            .loginProcessingUrl("/login")
                            .successForwardUrl("/user/dashboard")
                            .failureForwardUrl("/login?error=true")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .failureHandler(aouthfailureHandler)
                            .permitAll();
                }).logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout=true");
                    logout.permitAll();
                });

                httpSecurity.oauth2Login(oauth2->{
                    oauth2.loginPage("/login");
                    oauth2.successHandler(handler);
                });
        return httpSecurity.build();
        // default form login
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

