package dev.kush.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableMethodSecurity()
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(
                        req -> req.requestMatchers("/").hasAuthority("SCOPE_read")
                                .requestMatchers("/demo").hasAnyAuthority("SCOPE_write", "SCOPE_read")
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(rs -> rs
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authoritiesInString = jwt.getClaimAsString("authorities");

            return Arrays.stream(authoritiesInString.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        });
        return converter;
    }
}

@RestController
class DemoController {

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/")
    public String demo(Authentication authentication) {

        authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .forEach(System.out::println);
        var jwt = (Jwt) authentication.getPrincipal();
        System.out.println(jwt.getClaimAsString("authorities"));
        return "hello " + authentication.getName();
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @GetMapping("/demo")
    public String demo2(Authentication authentication) {
        return "hello " + authentication.getName();
    }
}

