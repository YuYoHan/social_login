package com.example.social.config.jwt;

// 여기는 토큰이 header에 담겨서 오면 검증을 하는 곳이다.

// 클라이언트 요청 시 JWT 인증을 하기 위해 설치하는 커스텀 필터로
// UsernamePasswordAuthenticationFiler 이전에 실행된다.
// 이전에 실행된다는 뜻은 JwtAuthenticationFilter 를 통과하면
// UsernamePasswordAuthenticationFilter 이후의 필터는 통과한 것으로 본다는 뜻이다.
// 쉽게 말해서, Username + Password 를 통한 인증을 Jwt 를 통해 수행한다는 것이다.

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// JWT 방식은 세션과 다르게 Filter 하나를 추가해야 합니다.
// 이제 사용자가 로그인을 했을 때, Request 에 가지고 있는 Token 을 해석해주는 로직이 필요합니다.
// 이 역할을 해주는것이 JwtAuthenticationFilter입니다.
// 세부 비즈니스 로직들은 TokenProvider에 적어둡니다. 일종의 service 클래스라고 생각하면 편합니다.
/*
 *   순서
 *   1. 사용자의 Request Header에 토큰을 가져옵니다.
 *   2. 해당 토큰의 유효성 검사를 실시하고 유효하면
 *   3. Authentication 인증 객체를 만들고
 *   4. ContextHolder에 저장해줍니다.
 *   5. 해당 Filter 과정이 끝나면 이제 시큐리티에 다음 Filter로 이동하게 됩니다.
 *
 *   이렇게 거치고 나면 컨트롤러에서 정보를 가져올 수 있습니다.
 * */
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;

    // doFilter는 토큰을 검증하고
    // 토큰의 인증정보를 SecurityContext에 담아주는 역할을 한다.

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // request header에서 JWT를 추출
        // 요청 헤더에서 JWT 토큰을 추출하는 역할
        String jwt = resovleToken(httpServletRequest);

        log.info("jwt in JwtAuthenticationFilter : " + jwt);

        // 어떤 경로로 요청을 했는지 보여줌
        String requestURI = httpServletRequest.getRequestURI();
        log.info("uri JwtAuthenticationFilter : " + requestURI);

        if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication authentication = null;
            try {
                authentication = jwtProvider.getAuthentication(jwt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.info("authentication in JwtAuthenticationFilter : " + authentication);

            // Spring Security의 SecurityContextHolder를 사용하여 현재 인증 정보를 설정합니다.
            // 이를 통해 현재 사용자가 인증된 상태로 처리됩니다.
            // 위에서 jwtProvider.getAuthentication(jwt)가 반환이 UsernamePasswordAuthenticationToken로
            // SecurityContext에 저장이 되는데 SecurityContextHolder.getContext().setAuthentication(authentication);
            // 처리를 하는 이유는 다음과 같다.
            /*
             *   1.  인증 정보 검증: JWT 토큰이나 다른 인증 정보를 사용하여 사용자를 식별하고
             *       권한을 확인하기 위해서는 토큰을 해독하여 사용자 정보와 권한 정보를 추출해야 합니다.
             *       이 역할은 jwtProvider.getAuthentication(jwt)에서 수행됩니다.
             *       이 메서드는 JWT 토큰을 분석하여 사용자 정보와 권한 정보를 추출하고, 해당 정보로 인증 객체를 생성합니다.
             *
             *   2.  인증 정보 저장:
             *       검증된 인증 객체를 SecurityContextHolder.getContext().setAuthentication(authentication);를
             *       사용하여 SecurityContext에 저장하는 이유는, Spring Security에서 현재 사용자의 인증 정보를
             *       전역적으로 사용할 수 있도록 하기 위함입니다. 이렇게 하면 다른 부분에서도 현재 사용자의 인증 정보를 사용할 수 있게 되며,
             *       Spring Security가 제공하는 @AuthenticationPrincipal 어노테이션을 통해 현재 사용자 정보를 편리하게 가져올 수 있습니다.
             * */
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.error("유효한 JWT가 없습니다. : " + requestURI);
        }
        filterChain.doFilter(request, response);
    }

    // 토큰을 가져오기 위한 메소드
    // Authorization로 정의된 헤더 이름을 사용하여 토큰을 찾고
    // 토큰이 "Bearer "로 시작하거나 "Bearer "로 안온 것도 토큰 반환
    private String resovleToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HEADER_AUTHORIZATION);

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        } else if(StringUtils.hasText(token)) {
            return token;
        } else {
            return null;
        }
    }
}
