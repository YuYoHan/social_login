package com.example.social.config.security;

import com.example.social.config.jwt.JwtAccessDeniedHandler;
import com.example.social.config.jwt.JwtAuthenticationEntryPoint;
import com.example.social.config.jwt.JwtProvider;
import com.example.social.config.oauth2.PrincipalOauth2UserService;
import com.example.social.config.oauth2.verifirer.GoogleOAuth2UserService;
import com.example.social.config.oauth2.verifirer.NaverOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final JwtProvider jwtProvider;
    private final GoogleOAuth2UserService googleOAuth2UserService;
    @Lazy
    private final NaverOAuth2UserService naverOAuth2UserService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = Arrays.asList(
                clientRegistration()
                // 여기에 다른 소셜 로그인 제공자(ClientRegistration) 정보를 추가할 수 있습니다.
        );

        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Bean
    public ClientRegistration clientRegistration() {
        // ClientRegistration 객체를 생성하고 OAuth 2.0 클라이언트의 등록 정보를 설정합니다.
        ClientRegistration googleClient = ClientRegistration
                .withRegistrationId("google")
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                // Google의 authorizationUri
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
                .scope("openid", "profile", "email")
                .clientName("Google")
                .build();

        ClientRegistration naverClient = ClientRegistration
                .withRegistrationId("naver")
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/naver")
                // Naver의 authorizationUri
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .scope("openid", "profile", "email")
                .clientName("Naver")
                .build();

        if(googleClient.getClientName().equals("Google")) {
            return googleClient;
        } else if(naverClient.getClientName().equals("Naver")) {
            return naverClient;
        } else {
            return null;
        }
    }
//    @Bean ClientRegistration naverClientRegistration() {
//        ClientRegistration naverClient = ClientRegistration
//                .withRegistrationId("naver")
//                .clientId(naverClientId)
//                .clientSecret(naverClientSecret)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("http://localhost:8080/login/oauth2/code/naver")
//                // Naver의 authorizationUri
//                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
//                .tokenUri("https://nid.naver.com/oauth2.0/token")
//                .scope("openid", "profile", "email")
//                .clientName("Naver")
//                .build();
//        return naverClient;
//    }


//    @Bean
//    // OAuth2UserService 타입의 빈을 생성합니다. 이 빈은 OAuth2 로그인 처리에 사용됩니다.
//    // OAuth2UserService는 OAuth2 로그인 후에 사용자 정보를 가져오고 처리하는 인터페이스입니다.
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> googleOAuth2UserOAuth2UserService() {
//        // new DefaultOAuth2UserService() { ... }:
//        // OAuth2UserService 인터페이스를 구현하는 익명 클래스를 생성합니다.
//        // 이 클래스는 OAuth2 로그인 처리에 사용될 사용자 서비스를 정의합니다.
//        return new DefaultOAuth2UserService() {
//            @Override
//            // loadUser(OAuth2UserRequest userRequest) { ... }:
//            // OAuth2UserService 인터페이스의 loadUser 메서드를 오버라이드합니다.
//            // 이 메서드는 OAuth2 로그인 후에 호출되며, 사용자 정보를 가져옵니다.
//            public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//                // super.loadUser(userRequest)를 호출하여 OAuth2 로그인 후에 사용자 정보를 가져옵니다.
//                // OAuth2User 객체는 사용자의 인증된 속성 및 권한을 포함하고 있습니다.
//                OAuth2User user = super.loadUser(userRequest);
//
//                // OAuth2 로그인 후에 반환되는 OAuth2User 객체를 수정하거나 구성하여 반환합니다.
//                // 여기에서는 사용자의 권한, 속성 및 고유 식별자(sub)를 포함하는
//                // DefaultOAuth2User 객체를 반환하고 있습니다.
//                return new DefaultOAuth2User(
//                        user.getAuthorities(),
//                        user.getAttributes(),
//                        "sub"
//                );
//            }
//        };
//    }
//
//    // naver
//    @Bean
//    @Qualifier("naver")
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> naverOAuth2UserService() {
//        return new DefaultOAuth2UserService() {
//            @Override
//            public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//                OAuth2User user = super.loadUser(userRequest);
//
//                // 사용자 정보 처리
//                // user.getName(), user.getAttributes()를 사용하여 필요한 정보를 가져올 수 있음
//
//                String uniqueIdentifier = user.getAttribute("id");
//
//                return new DefaultOAuth2User(
//                        user.getAuthorities(),
//                        user.getAttributes(),
//                        uniqueIdentifier
//                );
//            }
//        };
//    }


    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {
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
                .antMatchers("/**").permitAll();

        http
                // JWT를 위한 Filter를 아래에서 만들어 줄건데
                // 이 Filter를 어느위치에서 사용하겠다고 등록을 해주어야 Filter가 작동이 됩니다.
                // JWT를 검증하기 위한 JwtSecurityConfig를 적용하고
                // jwtProvider를 사용하여 JWT 검증을 수행합니다.
                .apply(new JwtSecurityConfig(
                        jwtProvider,
                        googleOAuth2UserService,
                        clientRegistration(),
                        naverOAuth2UserService));

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
                .clientRegistrationRepository(clientRegistrationRepository())
                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userInfoEndpoint()
                // OAuth2 로그인 성공 시, 후작업을 진행할 서비스
                .userService(principalOauth2UserService)
                .and()
                .defaultSuccessUrl("/success-oauth");

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
