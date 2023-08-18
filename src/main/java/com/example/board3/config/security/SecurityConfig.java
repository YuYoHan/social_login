package com.example.board3.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
<<<<<<< HEAD
=======
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
>>>>>>> feature/join
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

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
<<<<<<< HEAD
                .rememberMe().disable()
                // JWT 토큰 방식을 사용하면 더이상 세션저장은 필요없으니 해당 기능을 꺼줍니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll();
=======
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
>>>>>>> feature/join



        return http.build();
    }
<<<<<<< HEAD
=======


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



>>>>>>> feature/join
}
