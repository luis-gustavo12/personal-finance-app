package com.github.Finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {



}
