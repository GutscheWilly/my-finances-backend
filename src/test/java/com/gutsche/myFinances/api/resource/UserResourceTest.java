package com.gutsche.myFinances.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gutsche.myFinances.api.dto.UserDTO;
import com.gutsche.myFinances.model.entity.Launch;
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

import java.math.BigDecimal;

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

        String userJson = buildObjectJson(userDTO);

        MockHttpServletRequestBuilder userRequest = buildJsonRequest(HttpMethod.POST, "/login", userJson);

        checkExpectedUserRequest(userRequest, HttpStatus.OK, loggedUser);
    }

    @Test
    public void shouldReturnBadRequestWhenValidateLoginThrowsException() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();

        Mockito.when(userService.validateLogin(email, password)).thenThrow(LoginException.class);

        String userJson = buildObjectJson(userDTO);

        MockHttpServletRequestBuilder userRequest = buildJsonRequest(HttpMethod.POST, "/login", userJson);

        checkExpectedRequestStatus(userRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldRegisterUserSuccessful() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();
        User userAfterRegister = User.builder().id(1L).name(name).email(email).password(password).build();

        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn(userAfterRegister);

        String userJson = buildObjectJson(userDTO);

        MockHttpServletRequestBuilder userRequest = buildJsonRequest(HttpMethod.POST, "", userJson);

        checkExpectedUserRequest(userRequest, HttpStatus.CREATED, userAfterRegister);
    }

    @Test
    public void shouldReturnBadRequestWhenIsNotPossibleRegisterUser() throws Exception {
        String name = "user";
        String email = "user@gmail.com";
        String password = "123456";

        UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).build();

        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenThrow(BusinessRuleException.class);

        String userJson = buildObjectJson(userDTO);

        MockHttpServletRequestBuilder userRequest = buildJsonRequest(HttpMethod.POST, "", userJson);

        checkExpectedRequestStatus(userRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnStatusOkWhenGetBalanceUser() throws Exception {
        User user = User.builder().id(1L).name("user").email("user@gmail.com").password("1234").build();

        Mockito.when(userService.findById(user.getId())).thenReturn(user);

        Mockito.when(launchService.sumValuesFromFilteredLaunch(Mockito.any(Launch.class))).thenReturn(BigDecimal.valueOf(100));

        MockHttpServletRequestBuilder balanceRequest = MockMvcRequestBuilders.get(API_PATH.concat("/" + user.getId() + "/balance"));

        checkExpectedRequestStatus(balanceRequest, HttpStatus.OK);
    }

    private String buildObjectJson(Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }

    private MockHttpServletRequestBuilder buildJsonRequest(HttpMethod httpMethod, String endpoint, String json) {
        return MockMvcRequestBuilders
                .request(httpMethod, API_PATH.concat(endpoint))
                .accept(JSON)
                .contentType(JSON)
                .content(json);
    }

    private void checkExpectedUserRequest(MockHttpServletRequestBuilder userRequest, HttpStatus expectedHttpStatus, User expectedUser) throws Exception {
        checkExpectedRequestStatus(userRequest, expectedHttpStatus);

        mockMvc.perform(userRequest).andExpectAll(
                MockMvcResultMatchers.jsonPath("id").value(expectedUser.getId()),
                MockMvcResultMatchers.jsonPath("name").value(expectedUser.getName()),
                MockMvcResultMatchers.jsonPath("email").value(expectedUser.getEmail())
        );
    }

    private void checkExpectedRequestStatus(MockHttpServletRequestBuilder request, HttpStatus expectedHttpStatus) throws Exception {
        mockMvc.perform(request).andExpect(
                MockMvcResultMatchers.status().is(expectedHttpStatus.value())
        );
    }
}
