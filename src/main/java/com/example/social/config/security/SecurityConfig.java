package com.example.social.config.security;

import com.example.social.config.jwt.JwtAccessDenieHandler;
import com.example.social.config.jwt.JwtAuthenticationEntryPoint;
import com.example.social.config.jwt.JwtProvider;
import com.example.social.config.jwt.JwtSecurityConfig;
import com.example.social.config.oauth2.OAuth2SuccessHandler;
import com.example.social.config.oauth2.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final PrincipalOAuth2UserService principalOauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .anyRequest().permitAll();

        http
                // JWT Token을 위한 Filter를 아래에서 만들어 줄건데,
                // 이 Filter를 어느위치에서 사용하겠다고 등록을 해주어야 Filter가 작동이 됩니다.
                .apply(new JwtSecurityConfig(jwtProvider));

        // 에러 방지
        http
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDenieHandler());

        http
                // oauth2Login() 메서드는 OAuth 2.0 프로토콜을 사용하여 소셜 로그인을 처리하는 기능을 제공합니다.
                .oauth2Login()
                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userInfoEndpoint()
                // OAuth2 로그인 성공 시, 후작업을 진행할 서비스
                .userService(principalOauth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}
