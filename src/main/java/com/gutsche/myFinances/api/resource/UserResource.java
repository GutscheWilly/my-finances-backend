package com.gutsche.myFinances.api.resource;

import com.gutsche.myFinances.api.dto.UserDTO;
import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.service.UserService;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import com.gutsche.myFinances.service.exceptions.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> validateLogin(@RequestBody UserDTO userDTO) {
        try {
            User loggedUser = userService.validateLogin(userDTO.getEmail(), userDTO.getPassword());
            return ResponseEntity.ok().body(loggedUser);
        }
        catch (LoginException loginException) {
            return ResponseEntity.badRequest().body(loginException.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        User user = buildUser(userDTO);

        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        }
        catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    private User buildUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
    }
}
