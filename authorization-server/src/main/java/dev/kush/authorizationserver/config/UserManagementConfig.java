package dev.kush.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Configuration
public class UserManagementConfig {


    @Bean
    RegisteredClientRepository registeredClientRepository() {
        RegisteredClient c1 = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("client-1")
                .clientSecret("{noop}secret-1")
                .clientIdIssuedAt(Instant.now())
                .redirectUri("http://localhost:9091/login/oauth2/code/client-1")
                .scopes(scopes -> scopes.addAll(List.of("openid","read")))
                .clientName("Spring Boot Client 1")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(false)
                        .requireAuthorizationConsent(true)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .build())
                .build();
        return new InMemoryRegisteredClientRepository(c1);
    }

    @Bean
    UserDetailsManager userDetailsManager() {
        UserDetails u1 = User
                .withDefaultPasswordEncoder()
                .username("kush")
                .password("1234")
                .authorities("SCOPE_read")
                .build();
        return new InMemoryUserDetailsManager(u1);
    }

    @Bean
    OAuth2AuthorizationService oAuth2AuthorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }
}
