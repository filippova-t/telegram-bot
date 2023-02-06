package pro.sky.telegrambot.model;

import com.pengrad.telegrambot.request.BaseRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue (strategy= GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String notificationText;
    private LocalDateTime dateTime;

    public NotificationTask() {

    }

    public NotificationTask(Long id, Long chatId, String notion, LocalDateTime dateTime) {
        this.id = id;
        this.chatId = chatId;
        this.notificationText = notion;
        this.dateTime = dateTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notion) {
        this.notificationText = notion;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTask)) return false;
        NotificationTask table = (NotificationTask) o;
        return Objects.equals(getId(), table.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
