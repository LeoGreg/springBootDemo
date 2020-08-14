package com.example.demo.configs;


import com.example.demo.util.encoder.CustomEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true/*,securedEnabled = true,prePostEnabled = true*/)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


/*    @Autowired
    private MyBasicAuthenticationEntryPoint authenticationEntryPoint;*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .httpBasic().and()
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/api/accounts/**").permitAll()
                .antMatchers(HttpMethod.DELETE).hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/movie/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/api/files/**").denyAll()
                .antMatchers("/api/**").authenticated();

    }


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password("user1password").authorities("ROLE_USER").and()
                .withUser("admin").password("adminPassword").authorities("ROLE_ADMIN").and()
                .passwordEncoder(CustomEncoder.getInstance());
    }


}