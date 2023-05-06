package app.kezdesy.config;

import app.kezdesy.filter.AuthenticationFilter;
import app.kezdesy.filter.AuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/css/**", "/js/**", "/img/**", "**/favicon.ico", "/favicon.ico", "**/css/font-awesome.min.css").anonymous();
        http.authorizeRequests().antMatchers("/public/**", "/resources/**","/resources/static/**", "/", "/createRoom", "/room/*", "/setPicture", "/ws/**", "/ws/info").permitAll();
        http.authorizeRequests().antMatchers("/register", "/auth", "/login", "/token/refresh", "/loginUser", "/profile", "/updateUser", "/chats", "/chats/**", "/websocket", "/getChat", "/registerGoogle").permitAll();
        http.authorizeRequests().antMatchers("/home").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers("/allUsers").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new AuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
