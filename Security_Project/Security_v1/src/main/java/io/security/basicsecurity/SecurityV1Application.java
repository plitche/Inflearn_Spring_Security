package io.security.basicsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityV1Application {

    public static void main(String[] args) {
        SpringApplication.run(SecurityV1Application.class, args);


        /*
        String password = "1111";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);
        */
    }

}
