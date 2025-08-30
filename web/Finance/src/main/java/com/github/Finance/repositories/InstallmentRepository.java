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

//    Original query
//
//    SELECT
//    *,
//    DATE_ADD(first_split_date, INTERVAL splits MONTH) AS end_date
//    FROM
//            installments
//    WHERE
//    -- The installment period must have already started
//    first_split_date <= CURDATE()
//    -- And the current date must be before the calculated end date
//    AND CURDATE() < DATE_ADD(first_split_date, INTERVAL splits MONTH);

    @Query("SELECT i FROM Installment i WHERE i.user = :user AND i.firstSplitDate <= CURRENT DATE " +
            "AND (YEAR(CURRENT_DATE) * 12 + MONTH(CURRENT_DATE)) < (YEAR(i.firstSplitDate) * 12 + MONTH(i.firstSplitDate) + i.splits) ")
    public List<Installment> findUserActiveInstallments(User user);

}
