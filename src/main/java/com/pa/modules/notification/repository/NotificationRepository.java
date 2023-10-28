package com.pa.modules.notification.repository;

import com.pa.modules.notification.model.Notification;
import com.pa.modules.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findAllByUsers(Users user);
}
