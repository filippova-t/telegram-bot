package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final Pattern PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private TelegramBot telegramBot;
    private NotificationTaskRepository notificationTaskRepository;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            try {
                if ("/start".equals(update.message().text())) {
                    Long chatId = update.message().chat().id();
                    SendMessage responseMessage = new SendMessage(chatId,
                            "Hello! Запланируйте задачу в формате: dd.mm.yyyy HH:mm");
                    telegramBot.execute(responseMessage);
                } else {
                    Matcher matcher = PATTERN.matcher(update.message().text());
                    while (matcher.find()) {
                        String date = matcher.group(1);
                        String text = matcher.group(3);
                        LocalDateTime dateTime = LocalDateTime.parse(date, DATE_TIME_FORMATTER);
                        NotificationTask notificationTask = new NotificationTask();
                        notificationTask.setChatId(update.message().chat().id());
                        notificationTask.setNotificationText(text);
                        notificationTask.setDateTime(dateTime.truncatedTo(ChronoUnit.MINUTES));
                        notificationTaskRepository.save(notificationTask);
                        SendMessage responseMessage = new SendMessage(notificationTask.getChatId(), "Уведомление сохранено!");
                        telegramBot.execute(responseMessage);
                    }
                }
            } catch (Exception e) {
                SendMessage responseMessage = new SendMessage(update.message().chat().id(), "Некорректный запрос. Попробуйте еще раз!");
                telegramBot.execute(responseMessage);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}


