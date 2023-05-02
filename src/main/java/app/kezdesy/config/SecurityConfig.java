package app.kezdesy.config;


import app.kezdesy.filter.AuthenticationFilter;
import app.kezdesy.filter.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;


@EnableWebSecurity
@Configuration
public class SecurityConfig {


    private  final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


       http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/public/**",
                        "/resources/**",
                        "/resources/static/**",
                        "/",
                        "/createRoom",
                        "/room/*",
                        "/setPicture",
                        "/ws/**",
                        "/ws/info",
                        "/registerGoogle",
                        "/register",
                        "/auth",
                        "/login",
                        "/token/refresh",
                        "/loginUser",
                        "/profile",
                        "/updateUser",
                        "/chats",
                        "/chats/**",
                        "/websocket",
                        "/getChat"
                )
                .permitAll()
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "**/favicon.ico",
                        "/favicon.ico",
                        "**/css/font-awesome.min.css"
                )
                .anonymous()

                .requestMatchers("/home").hasAnyRole("ROLE_USER")
                .requestMatchers("/allUsers").hasAnyRole("ROLE_ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new AuthenticationFilter(authenticationManagerBean()))
                .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
      ;

        return http.build();
    }





//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.authorizeRequests().antMatchers("/css/**", "/js/**", "/img/**", "**/favicon.ico", "/favicon.ico", "**/css/font-awesome.min.css").anonymous();
//        http.authorizeRequests().antMatchers("/public/**", "/resources/**","/resources/static/**", "/", "/createRoom", "/room/*", "/setPicture", "/ws/**", "/ws/info", "/registerGoogle").permitAll();
//        http.authorizeRequests().antMatchers("/register", "/auth", "/login", "/token/refresh", "/loginUser", "/profile", "/updateUser", "/chats", "/chats/**", "/websocket", "/getChat").permitAll();
//        http.authorizeRequests().antMatchers("/home").hasAnyAuthority("ROLE_USER");
//        http.authorizeRequests().antMatchers("/allUsers").hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().anyRequest().authenticated();
//        http.addFilter(new AuthenticationFilter(authenticationManagerBean()));
//        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }

}
