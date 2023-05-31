package com.example.board3.config.security;

import com.example.board3.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
// 시큐리티 활성화
@EnableWebSecurity
// 예전에는 extends WebSecurityConfigurerAdapter를 햇지만 이제는 지원하지 않음
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

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
                // 스프링 시큐리티에서 세션을 관리하지 않겠다는 뜻입니다.
                // 서버에서 관리되는 세션없이 클라이언트에서 요청하는 헤더에 token을
                // 담아보낸다면 서버에서 토큰을 확인하여 인증하는 방식을 사용할 것이므로
                // 서버에서 관리되어야할 세션이 필요없습니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                // 인증절차에 대한 설정
                .authorizeRequests()
                // /v1/api/users에 대해 로그인을 요구함
                .antMatchers("/v1/api/users").authenticated()
                .antMatchers("/v1/api/boards").authenticated()
                // 나머지 요청에 대해서는 로그인을 요구하지 않음
                .anyRequest().permitAll()

                .and()
                // formLogin 기능을 끈다고 해서 form 태그 내에 로그인 기능을 못쓴다는 것은 아닙니다.
                // formLogin을 끄면 초기 로그인 화면이 사라집니다.
                // 그것보다 궁극적인 이유는 아래에 설명할 JWT의 기능을 만들기 위해서 입니다.
                // formLogin은 세션 로그인 방식에서 로그인을 자동처리 해준다는 장점이 존재했는데,
                // JWT에서는 로그인 방식 내에 JWT 토큰을 생성하는 로직이 필요하기 때문에
                // 로그인 과정을 수동으로 클래스를 만들어줘야 하기 때문에 formLogin 기능을 제외 합니다.
                // formLogin 기능 자체가 REST API에 반대되는 특징을 가지고 있습니다.
                // formLogin의 defaultSuccessUrl 메소드로 로그인 성공 시 리다이렉트 할 주소를 입력하게 되는데
                // REST API에서는 서버가 페이지의 기능을 결정하면 안되기 때문에 결과적으로 필요하지 않은 formLogin은 disable합니다.
                .formLogin().disable()
                // httpBasic은 기본적으로 disable이지만 켜두면 위와 같이 알림창이 뜹니다.
                // 쿠키와 세션을 이용한 방식이 아니라 request header에 id와 password값을 직접 날리는 방식이라
                // 보안에 굉장히 취약합니다. REST API에서는 오로지 토큰 방식을 이용하기 때문에 보안에 취약한
                // httpBasic 방식은 해제한다고 보시면 됩니다.
                .httpBasic().disable()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        return http.build();
    }


    // 정적 자원(Resource)에 대해서 인증된 사용자가 정적 자원에 대해 '인가'에
    // 대한 설정을 담당하는 메서드
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return
                (web) -> web.ignoring().antMatchers("/images/**", "/js/**");
    }



}
