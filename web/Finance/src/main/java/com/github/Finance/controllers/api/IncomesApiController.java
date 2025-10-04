package com.github.Finance.controllers.api;

import com.github.Finance.models.Income;
import com.github.Finance.services.IncomesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@Slf4j
public class IncomesApiController {


    private final IncomesService incomesService;

    public IncomesApiController(IncomesService incomesService) {
        this.incomesService = incomesService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUserIncomes () {
        return ResponseEntity.ok(
            incomesService.getUserRecentIncomes(null)
        );
    }


}
