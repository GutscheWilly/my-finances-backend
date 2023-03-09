package com.gutsche.myFinances.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gutsche.myFinances.api.dto.UserDTO;
import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.service.LaunchService;
import com.gutsche.myFinances.service.UserService;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import com.gutsche.myFinances.service.exceptions.LoginException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
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

    private static final String API_PATH = "/api/users";

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
        User loggedUser = User.builder().id(1L).name(name).email(email).password(password).build();

        Mockito.when(userService.validateLogin(email, password)).thenReturn(loggedUser);

        String userJson = buildUserJson(userDTO);

        MockHttpServletRequestBuilder userRequest = buildUserRequest(HttpMethod.POST, "/login", userJson);

        checkExpectedUserRequest(userRequest, HttpStatus.OK, loggedUser);
    }

    @Test
    public void shouldReturnBadRequestWhenValidateLoginThrowsException() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();

        Mockito.when(userService.validateLogin(email, password)).thenThrow(LoginException.class);

        String userJson = buildUserJson(userDTO);

        MockHttpServletRequestBuilder userRequest = buildUserRequest(HttpMethod.POST, "/login", userJson);

        checkExpectedBadRequestStatus(userRequest);
    }

    @Test
    public void shouldRegisterUserSuccessful() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();
        User userAfterRegister = User.builder().id(1L).name(name).email(email).password(password).build();

        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn(userAfterRegister);

        String userJson = buildUserJson(userDTO);

        MockHttpServletRequestBuilder userRequest = buildUserRequest(HttpMethod.POST, "", userJson);

        checkExpectedUserRequest(userRequest, HttpStatus.CREATED, userAfterRegister);
    }

    @Test
    public void shouldReturnBadRequestWhenIsNotPossibleRegisterUser() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();

        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenThrow(BusinessRuleException.class);

        String userJson = buildUserJson(userDTO);

        MockHttpServletRequestBuilder userRequest = buildUserRequest(HttpMethod.POST, "", userJson);

        checkExpectedBadRequestStatus(userRequest);
    }

    private String buildUserJson(UserDTO userDTO) throws Exception {
        return new ObjectMapper().writeValueAsString(userDTO);
    }

    private MockHttpServletRequestBuilder buildUserRequest(HttpMethod httpMethod, String endpoint, String userJson) {
        return MockMvcRequestBuilders
                .request(httpMethod, API_PATH.concat(endpoint))
                .accept(JSON)
                .contentType(JSON)
                .content(userJson);
    }

    private void checkExpectedUserRequest(MockHttpServletRequestBuilder userRequest, HttpStatus expectedStatus, User expectedUser) throws Exception {
        mockMvc.perform(userRequest).andExpectAll(
                MockMvcResultMatchers.status().is(expectedStatus.value()),
                MockMvcResultMatchers.jsonPath("id").value(expectedUser.getId()),
                MockMvcResultMatchers.jsonPath("name").value(expectedUser.getName()),
                MockMvcResultMatchers.jsonPath("email").value(expectedUser.getEmail())
        );
    }

    private void checkExpectedBadRequestStatus(MockHttpServletRequestBuilder userRequest) throws Exception {
        mockMvc.perform(userRequest).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
}
