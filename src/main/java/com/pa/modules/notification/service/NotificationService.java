package com.pa.modules.notification.service;


import com.pa.modules.notification.model.Notification;
import com.pa.modules.notification.repository.NotificationRepository;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private NotificationRepository notificationRepository;
    private UserService userService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    public List<Notification> getNotificationOfUser(Users user, int justUnread) {
        if (justUnread == 1)
            return notificationRepository.findAllByUsers(user).stream().filter(notification -> notification.getIsRead() == 0).collect(Collectors.toList());
        else
            return notificationRepository.findAllByUsers(user) ;
    }

    public Optional<Notification> findNotification(Long id) {
        return notificationRepository.findById(id);
    }

    public Notification setReadNotifications(Notification notification) {
        notification.setIsRead(1);
        notificationRepository.save(notification);
        return notification;
    }
}
