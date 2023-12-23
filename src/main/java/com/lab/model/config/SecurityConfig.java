package com.lab.model.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity /* Used for restrictions at endpoint level */
public class SecurityConfig {
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /* Unrestricted access */
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/register")
                        .permitAll()
                        .requestMatchers("/admin/**").hasAuthority("MANAGE_ACCOUNTS")
                        .requestMatchers("/images/public/**").permitAll()
                        .requestMatchers("/*")
                        .authenticated()
                )

                /*Change of the login page */
                .formLogin(form -> form
                        .loginPage("/login")
                        /* default is username, now we can use proper names in login.html */
                        .usernameParameter("email")
                        /* We want to handle the next navigation context:
                        *  - If is admin we go to the admin-dashboard page
                        *  - If is a type of employee we go to the home page */
                        .successHandler(customAuthenticationSuccessHandler) /* https://www.baeldung.com/spring-redirect-after-login */
                        .permitAll())
                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/logout") // Specify the logout URL
                        .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                        .invalidateHttpSession(true) // Invalidate session
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(); /* Accept int parameter as complexity strength, by default 10*/
    }
}
