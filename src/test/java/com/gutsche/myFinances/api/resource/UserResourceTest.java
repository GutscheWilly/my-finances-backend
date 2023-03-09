package com.gutsche.myFinances.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gutsche.myFinances.api.dto.UserDTO;
import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.service.LaunchService;
import com.gutsche.myFinances.service.UserService;
import com.gutsche.myFinances.service.exceptions.LoginException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserResource.class)
@AutoConfigureMockMvc
public class UserResourceTest {

    private static final String PATH_API = "/api/users";

    private static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private LaunchService launchService;

    @Test
    public void shouldValidateLoginSuccessful() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();
        User user = User.builder().id(1L).name(name).email(email).password(password).build();

        Mockito.when(userService.validateLogin(email, password)).thenReturn(user);

        String userJson = new ObjectMapper().writeValueAsString(userDTO);

        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders
                .post(PATH_API.concat("/login"))
                .accept(JSON)
                .contentType(JSON)
                .content(userJson);

        mockMvc.perform(userRequest).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("id").value(user.getId()),
                MockMvcResultMatchers.jsonPath("name").value(user.getName()),
                MockMvcResultMatchers.jsonPath("email").value(user.getEmail())
        );
    }

    @Test
    public void shouldReturnBadRequestWhenValidateLoginThrowsException() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();

        Mockito.when(userService.validateLogin(email, password)).thenThrow(LoginException.class);

        String userJson = new ObjectMapper().writeValueAsString(userDTO);

        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders
                .post(PATH_API.concat("/login"))
                .accept(JSON)
                .contentType(JSON)
                .content(userJson);

        mockMvc.perform(userRequest).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldRegisterUserSuccessful() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();
        User userAfterRegister = User.builder().id(1L).name(name).email(email).password(password).build();

        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn(userAfterRegister);

        String userJson = new ObjectMapper().writeValueAsString(userDTO);

        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders
                .post(PATH_API)
                .accept(JSON)
                .contentType(JSON)
                .content(userJson);

        mockMvc.perform(userRequest).andExpectAll(
                MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()),
                MockMvcResultMatchers.jsonPath("id").value(userAfterRegister.getId()),
                MockMvcResultMatchers.jsonPath("name").value(userAfterRegister.getName()),
                MockMvcResultMatchers.jsonPath("email").value(userAfterRegister.getEmail())
        );
    }
}
