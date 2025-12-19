package com.github.Finance.controllers.api;

import com.github.Finance.dtos.views.CardView;
import com.github.Finance.services.AuthenticationService;
import com.github.Finance.services.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardsApiController {

    private final CardService cardService;
    private final AuthenticationService authenticationService;

    public CardsApiController(CardService cardService, AuthenticationService authenticationService) {
        this.cardService = cardService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public List<CardView> getUserCards() {
        return cardService.getCardsByUser(authenticationService.getCurrentAuthenticatedUser());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

}
