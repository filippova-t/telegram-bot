package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Autowired
    NotificationTaskRepository notificationTaskRepository;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            try {
            if (update.message().text().equals("/start")) {
                Long chatId = update.message().chat().id();
                SendMessage responseMessage = new SendMessage(chatId, "Приветствуем вас!");
                telegramBot.execute(responseMessage);
            } else {
            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            Matcher matcher = pattern.matcher(update.message().text());
            while (matcher.find()) {
                String date = matcher.group(1);
                String text = matcher.group(3);
                LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                NotificationTask notificationTask = new NotificationTask();
                notificationTask.setChatId(update.message().chat().id());
                notificationTask.setNotification_text(text);
                notificationTask.setDateTime(dateTime);
                notificationTaskRepository.save(notificationTask);
                SendMessage responseMessage = new SendMessage(notificationTask.getChatId(), "Уведомление сохранено!");
                telegramBot.execute(responseMessage);
            }
        }}
        catch (Exception e) {
            SendMessage responseMessage = new SendMessage(update.message().chat().id(), "Некорректный запрос. Попробуйте еще раз!");
            telegramBot.execute(responseMessage);
        }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    @Scheduled (cron = "0 0/1 * * * *")
    public void getNotificationTasksWithCurrentTime () {
        Collection <NotificationTask> notificationTasks= notificationTaskRepository.findNotificationTasksWithCurrentTime();
        for (NotificationTask update : notificationTasks) {
            SendMessage responseMessage = new SendMessage(update.getChatId(), update.getNotification_text());
            telegramBot.execute(responseMessage);
        }
    }

}
