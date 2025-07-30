package com.github.Finance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 *
 * Class designed for the categories
 *
 * By default, there'll be 8 fixed categories, and each user can have 15 customized categories
 *
 */

@Table(name = "categories")
@Entity
@Getter
@Setter
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "user_id"
    )
    private User user;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private boolean isActive;


}
