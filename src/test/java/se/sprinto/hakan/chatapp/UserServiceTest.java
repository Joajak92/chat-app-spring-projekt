package se.sprinto.hakan.chatapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.sprinto.hakan.chatapp.model.User;
import se.sprinto.hakan.chatapp.repository.UserRepository;
import se.sprinto.hakan.chatapp.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @MockitoBean
    private CommandLineRunner commandLineRunner;
    @Autowired
    private UserService userService;
    @MockitoBean
    private UserRepository userRepository;

    @Test
    void shouldLogin(){
        User mockUser = new User("testanvändarnamn", "testlösenord");

        when(userRepository.findByUsernameAndPassword("testanvändarnamn", "testlösenord"))
                .thenReturn(mockUser);

        User loggedIn = userService.login("testanvändarnamn", "testlösenord");

        assertNotNull(loggedIn);
        assertEquals("testanvändarnamn", loggedIn.getUsername());
        verify(userRepository).findByUsernameAndPassword("testanvändarnamn", "testlösenord");
    }

    @Test
    void shouldRegister(){
        User inputUser = new User("testanvändarnamn", "testlösenord");
        User savedUser = new User("testanvändarnamn", "testlösenord");
        savedUser.setId(1L);

        when(userRepository.save(inputUser)).thenReturn(savedUser);

        User registeredUser = userService.register(inputUser);

        assertNotNull(registeredUser.getId());
        assertEquals(inputUser.getUsername(), registeredUser.getUsername());
        verify(userRepository).save(inputUser);
    }

    @Test
    void shouldFailLogin() {
        when(userRepository.findByUsernameAndPassword("felanvändarnamn",
                "fellösenord")).thenReturn(null);

        User result = userService.login("felanvändarnamn", "fellösenord");

        assertNull(result);
        verify(userRepository).findByUsernameAndPassword("felanvändarnamn", "fellösenord");
    }

}
