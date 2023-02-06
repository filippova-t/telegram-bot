package pro.sky.telegrambot.component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;


import java.util.Collection;

@Component
public class NotificationTaskTimer {

    private TelegramBot telegramBot;
    private NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskTimer(TelegramBot telegramBot, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void getNotificationTasksWithCurrentTime() {
        Collection<NotificationTask> notificationTasks = notificationTaskRepository.findNotificationTasksWithCurrentTime();
        for (NotificationTask update : notificationTasks) {
            SendMessage responseMessage = new SendMessage(update.getChatId(), update.getNotificationText());
            telegramBot.execute(responseMessage);
            notificationTaskRepository.delete(update);
        }
    }

}


