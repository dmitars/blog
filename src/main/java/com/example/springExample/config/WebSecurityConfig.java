package com.example.springExample.config;

import com.example.springExample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      //  auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("select username,password,active from users where username=?")
                .authoritiesByUsernameQuery("select u.username, ur.roles from users u inner join user_role ur " +
                        "on u.id=ur.user_id where u.username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/unpublished","/blog/*/remove","/blog/*/comments/*/remove")
                .hasAuthority("ADMIN")
                .antMatchers("/blog/*/edit","/blog/*/comment","/blog/add").authenticated()
                .antMatchers("/","/registration","/login","/blog/*").permitAll()
                .and().formLogin()
                .loginPage("/login")
                .and().exceptionHandling()
                .accessDeniedPage("/access-denied");
        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder encoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
