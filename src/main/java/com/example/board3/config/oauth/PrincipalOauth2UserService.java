package com.example.board3.config.oauth;

import com.example.board3.config.auth.PrincipalDetails;
import com.example.board3.domain.MemberDTO;
import com.example.board3.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberService memberService;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // registrationId로 어떤 OAuth로 로그인 했는지 확인가능
        log.info("clientRegistration : " + userRequest.getClientRegistration() );
        log.info("accessToken : " + userRequest.getAccessToken().getTokenValue() );

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : " + oAuth2User.getAttributes());

        // 회원가입 강제
        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("sub");
        String userName = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("get");
        String email = oAuth2User.getAttribute("email");

        MemberDTO byName = memberService.findByName(userName);

        if(byName == null) {
            MemberDTO member = MemberDTO.builder()
                    .userEmail(email)
                    .userPw(password)
                    .userName(userName)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            memberService.join(member);
        }
        return new PrincipalDetails(byName, oAuth2User.getAttributes());
    }

}
