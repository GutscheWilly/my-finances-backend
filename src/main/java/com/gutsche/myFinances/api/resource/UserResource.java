package com.gutsche.myFinances.api.resource;

import com.gutsche.myFinances.api.dto.UserDTO;
import com.gutsche.myFinances.model.entity.Launch;
import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.entity.enums.LaunchType;
import com.gutsche.myFinances.service.LaunchService;
import com.gutsche.myFinances.service.UserService;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import com.gutsche.myFinances.service.exceptions.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private UserService userService;

    private LaunchService launchService;

    @Autowired
    public UserResource(UserService userService, LaunchService launchService) {
        this.userService = userService;
        this.launchService = launchService;
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

    @PostMapping("/register")
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

    @GetMapping("/{id}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long id) {
        try {
            User user = userService.findById(id);

            Launch filteredRevenue = Launch.builder().user(user).type(LaunchType.REVENUE).build();
            Launch filteredExpense = Launch.builder().user(user).type(LaunchType.EXPENSE).build();

            BigDecimal revenues = launchService.sumValuesFromFilteredLaunch(filteredRevenue);
            BigDecimal expenses = launchService.sumValuesFromFilteredLaunch(filteredExpense);

            BigDecimal balance = revenues.subtract(expenses);

            return ResponseEntity.ok().body(balance);
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
