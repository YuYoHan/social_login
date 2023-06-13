package com.example.board3.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
        http
                .cors()
                .and()
                // 스프링 시큐리티에서 제공하는 로그인 페이지를 안쓰기 위해
                .httpBasic().disable()
                // JWT 방식을 제대로 쓰려고 하면, 프론트엔드가 분리된 환경을 가정하고 해야합니다.
                // 그래서 서버는 Restful한 Api형태가 되는데, 이를 위해 사용해줍니다.
                .csrf().disable()
                .formLogin().disable()
                .rememberMe().disable()
                // JWT 토큰 방식을 사용하면 더이상 세션저장은 필요없으니 해당 기능을 꺼줍니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .anyRequest().authenticated();



        return http.build();
    }
}
