package br.unibh.sdm.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.unibh.sdm.entities.User;
import br.unibh.sdm.repository.UserRepository;

public class UserServiceTest {
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_hashesPasswordAndSaves() {
        User u = new User("1", "Name", "n@example.com", "plainPass");
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.saveUser(u);

        assertNotNull(saved);
        assertEquals("Name", saved.getName());
        assertNotEquals("plainPass", saved.getPassword(), "Password should be hashed and not equal to raw");
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User("1","A","a@a.com","x"));
        list.add(new User("2","B","b@b.com","y"));

        when(userRepo.findAll()).thenReturn(list);

        Iterable<User> result = userService.getAllUsers();
        assertNotNull(result);
        List<User> resList = new ArrayList<>();
        result.forEach(resList::add);
        assertEquals(2, resList.size());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void testGetUserById_found() {
        User u = new User("1","A","a@a.com","p");
        when(userRepo.findByCode("1")).thenReturn(u);

        User res = userService.getUserById("1");
        assertNotNull(res);
        assertEquals("1", res.getCode());
    }

    @Test
    void testGetUserById_notFound() {
        when(userRepo.findByCode("missing")).thenReturn(null);
        User res = userService.getUserById("missing");
        assertNull(res);
    }

    @Test
    void testGetUserByEmail_found() {
        User u = new User("1","A","a@a.com","p");
        when(userRepo.findByEmail("a@a.com")).thenReturn(u);
        User res = userService.getUserByEmail("a@a.com");
        assertNotNull(res);
        assertEquals("a@a.com", res.getEmail());
    }

    @Test
    void testUpdateUser_success() {
        User existing = new User("1","Old","old@ex.com","oldpass");
        when(userRepo.findByCode("1")).thenReturn(existing);
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User incoming = new User(null, "New","new@ex.com","newpass");
        User updated = userService.updateUser("1", incoming);

        assertNotNull(updated);
        assertEquals("New", updated.getName());
        assertEquals("new@ex.com", updated.getEmail());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_notFound() {
        when(userRepo.findByCode("nope")).thenReturn(null);
        User updated = userService.updateUser("nope", new User());
        assertNull(updated);
    }

    @Test
    void testDeleteUser_success() {
        User existing = new User("1","X","x@x.com","p");
        when(userRepo.findByCode("1")).thenReturn(existing);
        doNothing().when(userRepo).delete(existing);

        boolean deleted = userService.deleteUser("1");
        assertTrue(deleted);
        verify(userRepo, times(1)).delete(existing);
    }

    @Test
    void testDeleteUser_notFound() {
        when(userRepo.findByCode("nope")).thenReturn(null);
        boolean deleted = userService.deleteUser("nope");
        assertFalse(deleted);
    }
}
