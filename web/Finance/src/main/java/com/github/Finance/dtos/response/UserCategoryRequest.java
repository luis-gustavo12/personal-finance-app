package com.github.Finance.dtos.response;

import com.github.Finance.models.Category;

public record UserCategoryRequest (
    Long id,
    String categoryName
) {

    public UserCategoryRequest(Category category) {
        this(category.getId(), category.getCategoryName());
    }

}
