package br.unibh.sdm.controller;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.unibh.sdm.entities.User;
import br.unibh.sdm.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_returnsCreated() throws Exception {
        User request = new User("1","Ana","ana@example.com","pass");
        User response = new User("1","Ana","ana@example.com","hashed");

        when(userService.saveUser(any(User.class))).thenReturn(response);

        mvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value("1"))
            .andExpect(jsonPath("$.email").value("ana@example.com"));
    }

    @Test
    void listUsers_returnsArray() throws Exception {
        User first = new User("1","A","a@a.com","p");
        User second = new User("2","B","b@b.com","q");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(first, second));

        mvc.perform(get("/user").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getUserById_found() throws Exception {
        User user = new User("1","A","a@a.com","p");

        when(userService.getUserById("1")).thenReturn(user);

        mvc.perform(get("/user/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("1"));
    }

    @Test
    void getUserById_notFound() throws Exception {
        when(userService.getUserById("2")).thenReturn(null);

        mvc.perform(get("/user/{id}", "2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void getUserByEmail_found() throws Exception {
        User user = new User("1","A","a@a.com","p");

        when(userService.getUserByEmail("a@a.com")).thenReturn(user);

        mvc.perform(get("/user/email/{email}", "a@a.com")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("a@a.com"));
    }

    @Test
    void updateUser_success() throws Exception {
        User request = new User(null,"New","new@ex.com","newpass");
        User response = new User("1","New","new@ex.com","hashed");

        when(userService.updateUser(eq("1"), any(User.class))).thenReturn(response);

        mvc.perform(put("/user/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("1"))
            .andExpect(jsonPath("$.email").value("new@ex.com"));
    }

    @Test
    void updateUser_notFound() throws Exception {
        when(userService.updateUser(eq("missing"), any(User.class))).thenReturn(null);

        mvc.perform(put("/user/{id}", "missing")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_success() throws Exception {
        when(userService.deleteUser("1")).thenReturn(true);

        mvc.perform(delete("/user/{id}", "1")).andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_notFound() throws Exception {
        when(userService.deleteUser("missing")).thenReturn(false);

        mvc.perform(delete("/user/{id}", "missing")).andExpect(status().isNotFound());
    }
}
