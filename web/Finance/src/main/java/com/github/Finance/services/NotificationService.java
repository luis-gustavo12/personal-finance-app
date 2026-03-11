package com.github.Finance.services;

import com.github.Finance.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
class NotificationService {

    private final NotificationRepository repository;

    NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

}
