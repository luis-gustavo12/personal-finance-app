package com.github.Finance.specifications;

import com.github.Finance.models.Income;

import com.github.Finance.models.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class IncomesSpecification {

    public static Specification<Income> hasMonth(Integer month) {

        return new  Specification<Income>() {
            @Override
            public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                if (month == null) return null;

                Expression<Integer> ex = criteriaBuilder.function("month", Integer.class, root.get("incomeDate"));

                return criteriaBuilder.equal(ex, month);
            }
        };

    }

    public static Specification<Income> hasYear(Integer year) {

        return new Specification<Income>() {
            
            @Override
            public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (year == null) return null;

                Expression<Integer> ex = criteriaBuilder.function("year", Integer.class, root.get("incomeDate"));

                return criteriaBuilder.equal(ex, year);

            }

        };

    }

    public static Specification<Income> hasCurrency(String currency) {

        return new Specification<Income>() {
            
            @Override
            public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                if (currency == null || currency.trim().isEmpty()) return null;

                return criteriaBuilder.equal(root.get("currency").get("currencyFlag"), currency);

            }

        };

    }

    public static Specification<Income> hasPaymentMethod(Long id) {

        return new Specification<Income>() {
            
            @Override
            public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                if (id == null) return null;

                return criteriaBuilder.equal(root.get("paymentMethod").get("id"), id);

            }

        };

    }

    public static Specification<Income> hasMinimum(Double amount) {

        return new Specification<Income>() {
            @Override
            public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                if (amount == null) return null;

                return criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amount);

            }
        };

    }

    public static Specification<Income> hasMaximum (Double amount) {

        return new Specification<Income>() {
            @Override
            public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                if (amount == null) return null;

                return criteriaBuilder.lessThanOrEqualTo(root.get("amount"), amount);

            }
        };


    }


    public static Specification<Income> setUser(User user) {

        return new Specification<Income>() {
            @Override
            public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("user"), user);
            }
        };

    }
}
