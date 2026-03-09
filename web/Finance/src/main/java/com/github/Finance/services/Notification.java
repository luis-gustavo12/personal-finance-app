package com.github.Finance.services;

import com.github.Finance.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
class Notification {

    private final NotificationRepository repository;

    Notification(NotificationRepository repository) {
        this.repository = repository;
    }

}
