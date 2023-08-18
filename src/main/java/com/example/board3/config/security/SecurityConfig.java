package com.example.board3.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.HashMap;
import java.util.Map;

@Configuration
// 시큐리티 활성화
@EnableWebSecurity
// 예전에는 extends WebSecurityConfigurerAdapter를 햇지만 이제는 지원하지 않음
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return  new BCryptPasswordEncoder();
    }


    // HTTP에 대해서 인증과 인가를 담당하는 메서드이며 필터를 통해 인증 방식과 인증 절차에 대해
    // 등록하며 설정을 담당하는 메서드
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
        http
                // CorsFilter라는 필터가 존재하는데 이를 활성화 시키는 작업
                .cors().and()
                // 세션을 사용하지 않고 JWT 토큰을 활용하여 진행하고
                // REST API를 만드는 작업이기 때문에 이 처리를 합니다.
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                // 스프링 시큐리티에서 세션을 관리하지 않겠다는 뜻입니다.
                // 서버에서 관리되는 세션없이 클라이언트에서 요청하는 헤더에 token을
                // 담아보낸다면 서버에서 토큰을 확인하여 인증하는 방식을 사용할 것이므로
                // 서버에서 관리되어야할 세션이 필요없습니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers("/api/v1/boards/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/items/**")
                .access("hasRole('ROLE_ADMIN')")
                // /success-oauth 엔드포인트에 대해 인증된 사용자만 접근 가능하도록 설정
//                  .antMatchers("/success-oauth").authenticated()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api/v1/users/**").permitAll();



        return http.build();
    }


    // 정적 자원(Resource)에 대해서 인증된 사용자가 정적 자원에 대해 '인가'에
    // 대한 설정을 담당하는 메서드
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return
                (web) -> web.ignoring().antMatchers("/images/**", "/js/**");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }



}
