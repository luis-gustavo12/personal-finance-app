package com.github.Finance.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.repositories.PaymentMethodRepository;

@Service
public class PaymentMethodsService {

    
    private final PaymentMethodRepository repository;

    public PaymentMethodsService(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        return repository.findAll();
    }

    public PaymentMethod findPaymentMethod(Long id) {
        return repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Payment couldn't be found!!") );
    }

    
}
