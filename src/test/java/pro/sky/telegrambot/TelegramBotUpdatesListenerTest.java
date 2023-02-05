package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

@WebMvcTest
public class TelegramBotUpdatesListenerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private NotificationTaskRepository notificationTaskRepository;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    public void testUpdatesListener() throws Exception {



    }
    }
