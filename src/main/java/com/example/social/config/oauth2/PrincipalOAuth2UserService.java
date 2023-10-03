package com.example.social.config.oauth2;

import com.example.social.config.jwt.JwtProvider;
import com.example.social.config.oauth2.provider.GoogleUser;
import com.example.social.config.oauth2.provider.NaverUser;
import com.example.social.config.oauth2.provider.OAuth2UserInfo;
import com.example.social.config.security.PrincipalDetails;
import com.example.social.domain.Role;
import com.example.social.domain.TokenDTO;
import com.example.social.entity.MemberEntity;
import com.example.social.entity.TokenEntity;
import com.example.social.repository.MemberRepositroy;
import com.example.social.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
// OAuth2UserService : Spring Security에서 OAuth 2.0을 사용하여 인증한 사용자 정보를 가져오기 위한 인터페이스입니다.
// 이 인터페이스를 구현하여 사용자 정보를 가져오는 방법을 정의하고,
// OAuth 2.0 프로바이더(예: Google, Facebook, GitHub 등)로부터 인증된 사용자 정보를 추출할 수 있습니다.

// OAuth2UserRequest : 이 객체는 OAuth 2.0 클라이언트 정보, 권한 부여 코드, 액세스 토큰 등을 포함합니다.
// 이 정보를 사용하여 사용자 정보를 요청하고 처리할 수 있습니다.

// OAuth2User : OAuth 2.0 프로바이더(인증 제공자)로부터 가져온 인증된 사용자 정보를 나타냅니다.
// 이 정보는 사용자의 프로필 데이터, 권한(스코프), 사용자 ID 등을 포함할 수 있습니다.
public class PrincipalOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepositroy memberRepositroy;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // ClientRegistration은 Spring Security에서 OAuth 2.0 또는 OpenID Connect (OIDC) 클라이언트
        // 애플리케이션의 등록 정보를 나타내는 클래스입니다. 클라이언트 애플리케이션의 설정 및 속성을 포함합니다.

        // userRequest.getClientRegistration()은 인증 및 인가된 사용자 정보를 가져오는
        // Spring Security에서 제공하는 메서드입니다.
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        log.info("clientRegistration : " + clientRegistration);
        String socialAccessToken = userRequest.getAccessToken().getTokenValue();
        log.info("소셜 로그인 accessToken : " + socialAccessToken);

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService =
                new DefaultOAuth2UserService();
        log.info("oAuth2UserService : " + oAuth2UserService);

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        log.info("oAuth2User : " + oAuth2User);
        log.info("getAttribute : " + oAuth2User.getAttributes());

        // 회원가입 강제 진행
        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = clientRegistration.getRegistrationId();

        if(registrationId.equals("google")) {
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUser(oAuth2User, clientRegistration);
        } else if(registrationId.equals("naver")) {
            log.info("네이버 로그인");
            oAuth2UserInfo = new NaverUser(oAuth2User, clientRegistration);
        } else {
            log.error("지언하지 않는 소셜 로그인입니다.");
        }

        // 사용자가 로그인한 소셜 서비스를 가지고 옵니다.
        // 예시) google or naver 같은 값을 가질 수 있다.
        String provider = oAuth2UserInfo.getProvider();
        // 사용자의 소셜 서비스(provider)에서 발급된 고유한 식별자를 가져옵니다.
        // 이 값은 해당 소셜 서비스에서 유니크한 사용자를 식별하는 용도로 사용됩니다.
        String providerID = oAuth2UserInfo.getProviderID();
        String name = oAuth2UserInfo.getName();
        // 사용자의 이메일 주소를 가지고 옵니다.
        // 소셜 서비스에서 제공하는 이메일 정보를 사용합니다.
        String email = oAuth2UserInfo.getEmail();
        // 소셜 로그인의 경우 무조건 USER 등급으로 고정이다.
        Role role = Role.USER;

        MemberEntity findUser = memberRepositroy.findByEmail(email);

        if(findUser == null) {
            log.info("OAuth 로그인이 최초입니다.");
            log.info("OAuth 자동 회원가입을 진행합니다.");

            findUser = MemberEntity.builder()
                    .email(email)
                    .userName(name)
                    .role(role)
                    .provider(provider)
                    .providerId(providerID)
                    .build();

            log.info("member : " + findUser);
            memberRepositroy.save(findUser);
        } else {
            log.info("로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어 있습니다.");
        }
        List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);
        TokenDTO tokenForOAuth2 = jwtProvider.createTokenForOAuth2(findUser.getEmail(), authoritiesForUser);
        TokenEntity findToken = tokenRepository.findByMemberEmail(tokenForOAuth2.getMemberEmail());

        TokenEntity saveToken = null;
        if(findToken == null) {
            TokenEntity tokenEntity = TokenEntity.toTokenEntity(tokenForOAuth2);
            saveToken = tokenRepository.save(tokenEntity);
            log.info("token : " + saveToken);
        } else {
            tokenForOAuth2 = TokenDTO.builder()
                    .id(findToken.getId())
                    .grantType(tokenForOAuth2.getGrantType())
                    .accessToken(tokenForOAuth2.getAccessToken())
                    .refreshToken(tokenForOAuth2.getRefreshToken())
                    .memberEmail(tokenForOAuth2.getMemberEmail())
                    .build();
            TokenEntity tokenEntity = TokenEntity.toTokenEntity(tokenForOAuth2);
            saveToken = tokenRepository.save(tokenEntity);
            log.info("token : " + saveToken);
        }

        // 토큰이 제대로 되어 있나 검증
        if(StringUtils.hasText(saveToken.getAccessToken())
                && jwtProvider.validateToken(saveToken.getAccessToken())) {
            Authentication authentication = jwtProvider.getAuthentication(saveToken.getAccessToken());
            log.info("authentication : " + authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = new User(email, "", authoritiesForUser);
            log.info("userDetails : " + userDetails);
            Authentication authentication1 =
                    new UsernamePasswordAuthenticationToken(userDetails, authoritiesForUser);
            log.info("authentication1 : " + authentication1);
            SecurityContextHolder.getContext().setAuthentication(authentication1);
        } else {
            log.info("검증 실패");
        }

        // attributes가 있는 생성자를 사용하여 PrincipalDetails 객체 생성
        // 소셜 로그인인 경우에는 attributes도 함께 가지고 있는 PrincipalDetails 객체를 생성하게 됩니다.
        PrincipalDetails principalDetails = new PrincipalDetails(findUser, oAuth2User.getAttributes());
        log.info("principalDetails : " + principalDetails);
        return principalDetails;
    }

    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity findUser) {
        Role role = findUser.getRole();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        log.info("권한 : " + role.name());
        return authorities;
    }
}
