package com.example.social.config.oauth2;

import com.example.social.entity.MemberEntity;
import com.example.social.entity.TokenEntity;
import com.example.social.repository.MemberRepositroy;
import com.example.social.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepositroy memberRepositroy;
    private final TokenRepository tokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");

        try {
            String email = authentication.getName();
            log.info("email : " + email);
            TokenEntity findToken = tokenRepository.findByMemberEmail(email);
            log.info("token : " + findToken);
            MemberEntity findUser = memberRepositroy.findByEmail(email);
            log.info("user : " + findUser);

            response.addHeader("providerId", findUser.getProviderId());
            response.addHeader("accessToken", "Bearer " + findToken.getAccessToken());
            response.addHeader("refreshToken", "Bearer " + findToken.getRefreshToken());
            response.addHeader("email", findToken.getMemberEmail());
        } catch (Exception e) {
            // 예외가 발생하면 클라이언트에게 오류 응답을 반환
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("OAuth 2.0 로그인 성공 후 오류 발생: " + e.getMessage());
            response.getWriter().flush();
        }
    }
}
