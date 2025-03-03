package org.Webbtj.DiggarenManar.security;

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
        super.configure(http);
        setLoginView(http, RadioSongView.class);
    }
}