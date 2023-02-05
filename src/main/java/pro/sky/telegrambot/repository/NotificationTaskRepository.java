package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.NotificationTask;


import java.util.Collection;

public interface NotificationTaskRepository extends JpaRepository <NotificationTask, Long> {

@Query
(value = "SELECT * FROM notification_task WHERE date_time <= CURRENT_TIMESTAMP ",
        nativeQuery = true)
Collection <NotificationTask> findNotificationTasksWithCurrentTime ();

}

