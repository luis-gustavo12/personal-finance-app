package com.github.Finance.controllers.api;

import com.github.Finance.models.PaymentMethod;
import com.github.Finance.services.PaymentMethodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodsApiController {

    private final PaymentMethodsService paymentMethodsService;

    public PaymentMethodsApiController(PaymentMethodsService paymentMethodsService) {
        this.paymentMethodsService = paymentMethodsService;
    }

    @GetMapping("")
    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethodsService.getAllPaymentMethods();
    }

}
