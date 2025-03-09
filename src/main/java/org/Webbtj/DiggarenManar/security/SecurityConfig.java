/*package org.Webbtj.DiggarenManar.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.Webbtj.DiggarenManar.view.LoginView;
import org.Webbtj.DiggarenManar.view.RadioSongView;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        // Configure the authorization rules
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**", "/radiosong").permitAll() // Permit these paths
                .anyRequest().authenticated() // Secure everything else
        );
    }
}

<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
            </dependency>
 */
