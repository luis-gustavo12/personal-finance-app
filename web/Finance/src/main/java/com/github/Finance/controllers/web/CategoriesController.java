package com.github.Finance.controllers.web;

import com.github.Finance.dtos.forms.AddCategoryForm;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.Category;
import com.github.Finance.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
@Slf4j
public class CategoriesController {


    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String mainCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllUserCategories());
        return "categories";
    }

    @GetMapping("/create")
    public String createCategories(Model model) {
        return "create-categories";
    }

    @PostMapping("/create")
    public String createCategory(AddCategoryForm form) {
        categoryService.createNewCategory(form);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategories(@PathVariable("id") Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found");
        }
        if (category.getUser() == null) {
            log.info("Default category cannot be changed!!");
            throw new ResourceNotFoundException("Default category cannot be changed!!");
        }
        return "edit-category";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(AddCategoryForm form, @PathVariable("id") Long categoryId) {
        categoryService.editCategory(categoryId, form);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategories(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return "redirect:/categories";
    }


}
