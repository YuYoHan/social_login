//package com.example.social.config.oauth2;
//
//import com.example.social.config.oauth2.verifirer.GoogleOAuth2UserService;
//import com.example.social.config.oauth2.verifirer.NaverOAuth2UserService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtParser;
//import io.jsonwebtoken.Jwts;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Log4j2
//@RequiredArgsConstructor
//public class OAuth2TokenAuthentication extends OncePerRequestFilter {
//    public static final String HEADER_AUTHORIZATION = "Authorization";
//    private final GoogleOAuth2UserService googleOAuth2UserService;
//    private final ClientRegistration clientRegistration;
//    private final NaverOAuth2UserService naverOAuth2UserService;
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        String jwt = resovleToken(httpServletRequest);
//
//        String getIssuer = checkToken(jwt);
//        if ("https://accounts.google.com".equals(getIssuer)) {
//            // Google에서 발급한 토큰을 기반으로 OAuth2AccessToken 객체를 생성합니다.
//            // 이 객체는 토큰의 타입, 값, 발급 시간 및 만료 시간과 같은 정보를 포함합니다.
//            OAuth2AccessToken accessToken = new OAuth2AccessToken(
//                    // 토큰 타입
//                    OAuth2AccessToken.TokenType.BEARER,
//                    // 토큰 값
//                    jwt,
//                    // 발급 시간
//                    null,
//                    // 만료 시간
//                    null);
//
//            // OAuth2UserRequest 생성
//            // 이 객체는 클라이언트 등록 정보와 OAuth2AccessToken 객체를 포함하여
//            // OAuth 2.0 사용자 정보 요청에 필요한 정보를 제공합니다.
//            OAuth2UserRequest userRequest = new OAuth2UserRequest(
//                    // ClientRegistration 객체
//                    clientRegistration,
//                    // OAuth2AccessToken 객체
//                    accessToken);
//
//            // googleOAuth2UserService를 사용하여 사용자 정보를 검증하고 OAuth2User 객체를 얻어옵니다.
//            OAuth2User oAuth2User = googleOAuth2UserService.loadUser(userRequest);
//
//            // SecurityContextHolder에 인증 정보 설정
//            //  검증된 사용자 정보를 기반으로 OAuth2AuthenticationToken을 생성합니다.
//            //  이 토큰은 Spring Security에서 사용되며, 사용자 정보와 사용자의 권한을 포함합니다.
//            Authentication authentication = new OAuth2AuthenticationToken(
//                    oAuth2User,
//                    oAuth2User.getAuthorities(),
//                    // registrationId
//                    "google");
//            // SecurityContextHolder에 인증 정보 설정
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        if ("https://api.naver.com".equals(getIssuer)) {
//            // 다른 발급자 (Google이 아닌) 경우
//            // 네이버로부터 받은 토큰을 OAuth2AccessToken 객체로 생성합니다.
//            // 이 객체는 토큰의 타입, 값, 발급 시간 및 만료 시간 등을 포함합니다.
//            // 이 정보는 OAuth2 인증을 수행하는 데 사용됩니다.
//            OAuth2AccessToken naverAccessToken = new OAuth2AccessToken(
//                    OAuth2AccessToken.TokenType.BEARER,
//                    jwt,
//                    null,
//                    null);
//
//            // 네이버 OAuth2 클라이언트 등록 정보와 받은 토큰을 사용하여 OAuth2 사용자 정보 요청을 만듭니다.
//            // 이 요청은 네이버로부터 사용자 정보를 가져오는 데 사용됩니다.
//            OAuth2UserRequest naverUserRequest = new OAuth2UserRequest(
//                    clientRegistration,
//                    naverAccessToken);
//
//            // 네아로 API를 사용하여 사용자 정보 가져오기
//            // 이것은 네이버 API를 호출하고 사용자 정보를 가져오는 로직이 포함된 서비스입니다.
//            OAuth2User naverUser = naverOAuth2UserService.loadUser(naverUserRequest);
//
//            // 네이버에서 가져온 사용자 정보를 기반으로 Authentication 객체 생성
//            // 네이버로부터 가져온 사용자 정보를 기반으로 OAuth2 인증 토큰을 생성합니다.
//            // 이 토큰은 Spring Security에서 사용되며 사용자 정보와 사용자의 권한을 포함합니다.
//            // 여기서 "naver"는 등록 ID로 지정되며, 네이버와 관련된 토큰임을 나타냅니다.
//            Authentication naverAuthentication = new OAuth2AuthenticationToken(
//                    naverUser,
//                    naverUser.getAuthorities(),
//                    // registrationId
//                    "naver");
//
//            // 네이버 Authentication 객체를 SecurityContext에 저장
//            SecurityContextHolder.getContext().setAuthentication(naverAuthentication);
//        }
//    }
//
//    // 토큰을 가져오기 위한 메소드
//    // Authorization로 정의된 헤더 이름을 사용하여 토큰을 찾고
//    // 토큰이 "Bearer "로 시작하거나 "Bearer "로 안온 것도 토큰 반환
//    private String resovleToken(HttpServletRequest httpServletRequest) {
//        String token = httpServletRequest.getHeader(HEADER_AUTHORIZATION);
//        log.info("token : " + token);
//
//        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
//            return token.substring(7);
//        } else if (StringUtils.hasText(token)) {
//            return token;
//        } else {
//            return null;
//        }
//    }
//
//    private String checkToken(String token) {
//        try {
//            JwtParser parser = Jwts.parserBuilder().build();
//            // 주어진 토큰을 디코딩하여 JWT 클레임을 추출합니다.
//            // 이 클레임에는 토큰에 대한 정보가 포함되어 있으며,
//            // 이 코드에서는 클레임에서 발급자(issuer) 정보를 확인하기 위해 사용합니다.
//            Claims claims = parser.parseClaimsJws(token).getBody();
//
//            // iss 클레임을 확인하여 Google 발급 토큰 여부를 판별
//            String issuer = (String) claims.get("iss");
//            log.info("issuer : " + issuer);
//
//            return issuer;
//        } catch (Exception e) {
//            // 예외 처리: JWT 디코딩 실패 시 처리할 내용을 여기에 추가
//            log.error("JWT 디코딩 실패: " + e.getMessage(), e);
//            throw new RuntimeException("JWT 디코딩 실패: " + e.getMessage(), e);
//        }
//    }
//}
