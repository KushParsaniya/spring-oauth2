package dev.kush.oauth2client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Oauth2ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ClientApplication.class, args);
    }

    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(rs ->
                        rs.path("/**")
                                .filters(GatewayFilterSpec::tokenRelay)
                                .uri("http://localhost:9092/**")
                )
                .build();
    }
}

//@RestController
//class DemoController {
//
//    @GetMapping("/")
//    public String demo() {
//        return "Hello";
//    }
//}
