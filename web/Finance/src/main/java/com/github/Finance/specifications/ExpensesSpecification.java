package com.github.Finance.specifications;

import com.github.Finance.models.Expense;
import com.github.Finance.models.Income;
import com.github.Finance.models.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class ExpensesSpecification {

    public static Specification<Expense> hasMonth(Integer month) {

        return new Specification<Expense>() {
            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                if (month == null)
                    return null;

                Expression<Integer> ex = criteriaBuilder.function("month", Integer.class, root.get("date"));

                return criteriaBuilder.equal(ex, month);
            }
        };

    }

    public static Specification<Expense> hasYear(Integer year) {
        return new Specification<Expense>() {

            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                if (year == null)
                    return null;

                Expression<Integer> ex = criteriaBuilder.function("year", Integer.class, root.get("date"));

                return criteriaBuilder.equal(ex, year);

            }
        };
    }

    public static Specification<Expense> hasCurrency(String currency) {
        return new Specification<Expense>() {

            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (currency == null || currency.trim().isEmpty())
                    return null;

                return criteriaBuilder.equal(root.get("currency").get("currencyFlag"), currency);

            }
        };
    }

    public static Specification<Expense> hasPaymentMethod(Long id) {
        return new Specification<Expense>() {

            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (id == null)
                    return null;

                return criteriaBuilder.equal(root.get("paymentMethod").get("id"), id);

            }
        };
    }

    public static Specification<Expense> hasMinimum(Double amount) {

        return new Specification<Expense>() {
            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                if (amount == null) return null;

                return criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amount);

            }
        };

    }

    public static Specification<Expense> hasMaximum(Double amount) {
        return new Specification<Expense>() {
            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (amount == null) return null;

                return criteriaBuilder.lessThanOrEqualTo(root.get("amount"), amount);
            }
        };
    }

    public static Specification<Expense> setUser(User user) {
        return new Specification<Expense>() {
            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("user"), user);
            }
        };
    }

}
