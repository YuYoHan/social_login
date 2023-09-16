package com.example.social.config.security;

import com.example.social.config.jwt.JwtAccessDeniedHandler;
import com.example.social.config.jwt.JwtAuthenticationEntryPoint;
import com.example.social.config.jwt.JwtProvider;
import com.example.social.config.oauth2.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // HTTP 기본 인증 비활성화
                .httpBasic().disable()
                // CSRF(Cross-Site Request Forgery) 공격 방어 비활성화
                .csrf().disable()
                // 폼 기반 로그인 비활성화
                .formLogin().disable()
                // 로그아웃 관련 설정 비활성화
                .logout().disable()
                // 세션 관리를 STATELESS로 설정하여 세션을 사용하지 않도록 설정
                // JWT를 사용할거기 때문
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers("/api/v1/users/**").permitAll();

        http
                // JWT를 위한 Filter를 아래에서 만들어 줄건데
                // 이 Filter를 어느위치에서 사용하겠다고 등록을 해주어야 Filter가 작동이 됩니다.
                // JWT를 검증하기 위한 JwtSecurityConfig를 적용하고
                // jwtProvider를 사용하여 JWT 검증을 수행합니다.
                .apply(new JwtSecurityConfig(jwtProvider));

        // 에러 방지
        http
                .exceptionHandling()
                // 인증 에러 핸들링을 위한 커스텀 JwtAuthenticationEntryPoint 등록.
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                // 권한 에러 핸들링을 위한 커스텀 JwtAccessDeniedHandler 등록
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        // OAuth2
        http
                // oauth2Login() 메서드는 OAuth 2.0 프로토콜을 사용하여 소셜 로그인을 처리하는 기능을 제공합니다.
                .oauth2Login()
                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userInfoEndpoint()
                // OAuth2 로그인 성공 시, 후작업을 진행할 서비스
                .userService(principalOauth2UserService);

//        http
//                //  Spring Security에게 OAuth2 리소스 서버를 설정하도록 지시하는 부분입니다.
//                //  즉, 이 설정 아래에서 JWT를 검증하는 데 필요한 구성을 수행합니다.
//                .oauth2ResourceServer()
//                //  OAuth2 리소스 서버가 JWT 토큰을 사용한다고 알려줍니다.
//                //  Spring Security에 JWT 검증을 수행하도록 설정합니다.
//                .jwt()
//                // JWT 디코더를 설정합니다.
//                // JWT 디코더는 JWT 토큰을 검증하고 내용을 추출하는 데 사용됩니다.
//                .decoder(this.jwtDecoder());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    // JWT 디코더를 생성하는 메서드를 정의합니다.
    //  Google의 공개 키를 가져와 JWT 토큰의 서명을 검증하기 위해 사용합니다.
    //  withJwkSetUri 메서드를 통해 Google의 공개 키를 가져오는 URI를 지정합니다.
    // 이러한 구성을 통해 Spring Security는 Google OAuth2에서 제공하는 JWT 토큰의 유효성을 검사하고,
    // 토큰이 유효하면 해당 사용자에 대한 정보를 추출할 수 있습니다.
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//                .build();
//    }
}
