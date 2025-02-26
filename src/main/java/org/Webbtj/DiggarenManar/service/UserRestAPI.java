package org.Webbtj.DiggarenManar.service;

import org.Webbtj.DiggarenManar.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fungerar inte ännu, in progress.
 */
@RestController
@RequestMapping("users")
public class UserRestAPI {

    private final UserService userService;

    @Autowired
    public UserRestAPI(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public User createUser(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        return userService.createUser(username, password, email);
    }
}
