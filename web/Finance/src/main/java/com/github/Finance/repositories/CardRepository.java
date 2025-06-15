package com.github.Finance.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.Card;
import com.github.Finance.models.User;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>  {
    
    List<Card> findAllByUser(User user);

}
