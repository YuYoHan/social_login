package com.example.social.config.oauth2.verifirer;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    private final HttpTransport httpTransport = new NetHttpTransport();
    private final JsonFactory jsonFactory = new JacksonFactory();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String accessToken = userRequest.getAccessToken().getTokenValue();

        // Google API 클라이언트 라이브러리를 사용하여 accessToken을 검증
        // GoogleIdTokenVerifier는 Google API의 ID 토큰을 검증하기 위한 도구입니다.
        // Google의 OAuth 2.0 서비스를 통해 발급된 ID 토큰의 유효성을 확인하고,
        // 해당 토큰이 애플리케이션의 클라이언트 ID에 대한 것인지 확인하는 역할을 합니다.

        // httpTransport: HTTP 통신을 처리하는 라이브러리를 지정하는 부분입니다.
        // Google API와 통신하기 위해 사용하는 HTTP 트랜스포트 라이브러리를 설정합니다.

        // jsonFactory: JSON 데이터를 파싱하고 생성하는 데 사용되는 라이브러리를 지정하는 부분입니다.
        // Google API와 통신할 때 JSON 형식의 데이터를 처리하는 데 사용됩니다.
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                // 이 부분에서는 검증할 ID 토큰의 대상(audience)을 설정합니다.
                // 여기서는 Google API 클라이언트 ID를 대상으로 설정하고 있으며,
                // 이것은 해당 ID 토큰이 특정 클라이언트(여기서는 구글 클라이언트)에 대한 것임을 나타냅니다.
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken;

        try {
            // Google의 ID 토큰 검증기(GoogleIdTokenVerifier)를 사용하여
            // 주어진 액세스 토큰(accessToken)을 검증하는 작업을 수행합니다.
            idToken = verifier.verify(accessToken);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(idToken != null) {
            //  Google ID 토큰(idToken)에서 클레임(claims)을 추출하기 위해
            //  idToken 객체에서 Payload를 얻어옵니다. 토큰의 페이로드는 토큰에 포함된 클레임 정보를 담고 있습니다.
            Payload payload = idToken.getPayload();
            // 사용자 정보를 저장할 attributes 맵을 생성합니다.
            // 이 맵에는 사용자의 이름, 이메일, 서브젝트(sub), 그리고 권한 정보가 포함될 것입니다.
            Map<String, Object> attributes = new HashMap<>();

            attributes.put("id", payload.get("id"));
            // 사용자 정보를 추출하고 OAuth2User 객체로 변환
            // 페이로드에서 이름(사용자의 실제 이름)을 추출
            attributes.put("name", payload.get("name"));
            // 페이로드에서 이메일을 추출
            attributes.put("email", payload.getEmail());
            // 페이로드에서 서브젝트(sub) 정보를 추출
            attributes.put("sub", payload.getSubject());
            // 사용자에게 ROLE_USER 권한을 부여하기 위해 attributes 맵에 권한 정보를 추가합니다.
            attributes.put("auth", new SimpleGrantedAuthority("ROLE_USER"));

            // DefaultOAuth2User는 Spring Security에서 OAuth 2.0 사용자를 나타내는 구현 클래스입니다.
            // 이 객체는 사용자의 인증 정보와 권한 정보를 가지고 있습니다.
            return new DefaultOAuth2User(
                    Collections.singleton(new OAuth2UserAuthority(attributes)),
                    attributes,
                    "sub"
            );
        } else {
            throw new OAuth2AuthenticationException("Google AccessToken verification failed");
        }
    }

}
