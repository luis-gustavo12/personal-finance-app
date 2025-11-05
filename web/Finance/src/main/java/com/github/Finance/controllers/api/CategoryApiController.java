package com.github.Finance.controllers.api;

import com.github.Finance.dtos.response.UserCategoryRequest;
import com.github.Finance.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController {


    private final CategoryService categoryService;

    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public List<UserCategoryRequest> getCategories() {
        return categoryService.getAllUserCategories(null).stream()
                .map(UserCategoryRequest::new).toList();
    }

}
