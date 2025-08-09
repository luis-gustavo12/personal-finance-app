package com.github.Finance.repositories;

import com.github.Finance.models.Installment;
import com.github.Finance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    @Query("SELECT i FROM Installment i WHERE i.user = :user")
    public List<Installment> findAllByUser(@Param("user") User user);

}
