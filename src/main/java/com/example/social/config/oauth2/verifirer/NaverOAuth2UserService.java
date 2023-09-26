package com.example.social.config.oauth2.verifirer;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class NaverOAuth2UserService  {

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String accessToken = userRequest.getAccessToken().getTokenValue();

        // 네이버 API 엔드포인트 URL
        String naverApiUrl = "https://openapi.naver.com/v1/nid/me";

        // 네이버 API 호출을 위한 HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        // headers 객체의 setContentType 메서드를 사용하여 요청의 Content-Type 헤더를 설정합니다.
        // MediaType.APPLICATION_FORM_URLENCODED는 HTTP 요청 본문이 폼 데이터로 인코딩되어 있다는 것을 나타냅니다.
        // 이러한 형태의 요청은 주로 HTML 폼 데이터를 서버로 제출할 때 사용됩니다.
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Authorization 헤더를 설정합니다. 이 헤더는 HTTP 요청에 인증 정보를 포함하는 데 사용됩니다.
        // 여기서 "Bearer " + accessToken는 OAuth 2.0 인증 프로토콜을 사용하여
        // 보호된 리소스에 액세스하기 위한 액세스 토큰을 Bearer 스타일로 설정하는 것을 나타냅니다.
        // Bearer는 토큰의 타입을 나타내며, 액세스 토큰의 실제 값을 나타냅니다.
        // 이렇게 설정된 Authorization 헤더를 통해 서버는 요청이 인증되었음을 확인하고,
        // 해당 액세스 토큰의 유효성을 검증할 수 있습니다.
        headers.set("Authorization", "Bearer " + accessToken);

        // 네이버 API 호출을 위한 요청 파라미터 설정
        // MultiValueMap은 HTTP 요청 본문의 데이터를 표현하기 위한 자료 구조입니다.
        // 네이버 API 호출 시 요청 본문에 보낼 데이터를 설정하기 위해 body라는 MultiValueMap 객체를 생성합니다.
        // 이 데이터는 폼 데이터 형식으로 API 서버로 전송될 것입니다.
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        // 네이버 API 호출
        // RestTemplate은 Spring Framework에서 제공하는
        // HTTP 요청을 보내고 응답을 받는 데 사용되는 클라이언트 라이브러리입니다.
        RestTemplate restTemplate = new RestTemplate();
        // RestTemplate을 사용하여 네이버 API를 호출하고 응답을 받습니다.

        // naverApiUrl: 네이버 API의 엔드포인트 URL을 나타냅니다.
        // 이 URL은 네이버 API의 특정 엔드포인트에 요청을 보내기 위해 사용됩니다.

        // body 객체가 요청 본문에 담길 것입니다.

        // API 응답을 받을 데이터 유형을 지정합니다.
        // 여기서는 API 응답을 Map 형태로 받을 것이며, 이 Map에는 API 응답의 JSON 데이터가 매핑됩니다.
        Map<String, Object> naverApiResult = restTemplate.patchForObject(naverApiUrl, body, Map.class);

        // 네이버 API 응답을 파싱하여 사용자 정보 추출
        // naverApiResult라는 Map에서 "id" 키에 해당하는 값을 추출합니다.
        // 이 값은 네이버 사용자의 고유 식별자인 사용자 ID를 나타냅니다.
        String naverUserId = (String) naverApiResult.get("id");
        String naverUserEmail = (String) naverApiResult.get("email");
        String naverUserName = (String) naverApiResult.get("name");

        // 추출한 사용자 정보로 OAuth2User 객체 생성
        // 이객체는 Spring Security OAuth2에서 사용자 정보를 나타내는 데 사용됩니다.
        OAuth2User naverUser = new DefaultOAuth2User(
                // 사용자에게 부여할 권한을 설정합니다.
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                // 사용자의 고유 식별자인 네이버 사용자 ID를 "naverUserId"라는 키와 함께 맵 형태로 저장합니다.
                Collections.singletonMap("naverUserId", naverUserId),
                // OAuth2User의 이름을 지정합니다. 이것은 OAuth2User 객체 내에서 사용됩니다.
                "naverUserId"
        );
        // 이메일 정보 추가
        // 생성한 OAuth2User 객체에서 추가 정보를 설정합니다.
        // 이 경우, "naverUserEmail" 키를 사용하여 네이버 사용자의 이메일 정보를 맵에 추가합니다.
        ((DefaultOAuth2User) naverUser).getAttributes().put("naverUserEmail", naverUserEmail);

        // 이렇게 생성된 naverUser 객체에는 사용자의 권한과 사용자 ID, 그리고 이메일 정보가 포함되어 있습니다.
        // 이 객체는 사용자 인증 후에 Spring Security의 SecurityContextHolder에
        // 저장되어 사용자 정보를 유지하고 제공합니다.
        return naverUser;
    }

}
