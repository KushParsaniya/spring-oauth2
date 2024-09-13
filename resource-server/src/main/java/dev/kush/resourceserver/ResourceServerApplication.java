package dev.kush.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

}

@RestController
class DemoController {

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/")
    public String demo(Authentication authentication) {
        return "hello " + authentication.getName();
    }
}
