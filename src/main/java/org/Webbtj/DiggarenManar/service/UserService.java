package org.Webbtj.DiggarenManar.service;

import org.Webbtj.DiggarenManar.domain.User;
import org.Webbtj.DiggarenManar.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String password, String email) {
        User user = new User(username, password, email);
        return userRepository.save(user);
    }
}
