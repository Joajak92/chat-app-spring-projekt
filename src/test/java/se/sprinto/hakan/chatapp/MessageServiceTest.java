package se.sprinto.hakan.chatapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.sprinto.hakan.chatapp.model.Message;
import se.sprinto.hakan.chatapp.model.User;
import se.sprinto.hakan.chatapp.repository.MessageRepository;
import se.sprinto.hakan.chatapp.repository.UserRepository;
import se.sprinto.hakan.chatapp.service.MessageService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
public class MessageServiceTest {

    @MockitoBean
    private CommandLineRunner commandLineRunner;
    @MockitoBean
    private MessageRepository messageRepository;
    @MockitoBean
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;

    @Test
    void shouldSaveMessage() {
        Message message = new Message();
        User user = new User("testanvändare", "testpassword");
        message.setText("Test meddelande");
        message.setUser(user);

        messageService.save(message);

        verify(messageRepository).save(message);
    }

    @Test
    void shouldGetMessagesWhenLoggingIn() {
        User user = new User("testanvändare",  "testpassword");
        user.setId(1L);

        Message message1 = new Message();
        message1.setText("Test meddelande 1");
        message1.setUser(user);

        Message message2 = new Message();
        message2.setText("Test meddelande 2");
        message2.setUser(user);

        List<Message> messages = List.of(message1,message2);

        when(messageRepository.findByUserId(1L)).thenReturn(messages);

        List<Message> result = messageService.getMessages(1L);

        assertEquals(2, result.size());
        assertEquals("Test meddelande 1", result.get(0).getText());
        assertEquals("Test meddelande 2", result.get(1).getText());
        verify(messageRepository).findByUserId(1L);
    }

}
