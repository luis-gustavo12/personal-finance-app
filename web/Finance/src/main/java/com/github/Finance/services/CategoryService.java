package com.github.Finance.services;

import com.github.Finance.dtos.forms.AddCategoryForm;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.Category;
import com.github.Finance.models.User;
import com.github.Finance.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthenticationService authenticationService;

    public CategoryService(CategoryRepository categoryRepository, AuthenticationService authenticationService) {
        this.categoryRepository = categoryRepository;
        this.authenticationService = authenticationService;
    }

    public List<Category> getAllCurrentLoggedUserCategories() {
        User user = authenticationService.getCurrentAuthenticatedUser();

        return categoryRepository.findDefaultAndUserCategories(user);

    }

    public List<Category> getAllUserCategories(User user) {
        return categoryRepository.findDefaultAndUserCategories(user);
    }

    /**
     * IDOR check on category
     * @return true for valid or false
     */
    public boolean validCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->  new ResourceNotFoundException("Not found!!"));

        if (category.getUser() == null) {
            log.info("Default category, no check required");
            return true;
        }

        User user = authenticationService.getCurrentAuthenticatedUser();

        if (user.getId().equals(category.getUser().getId())) {
            log.info("Category belongs to the same user");
            return true;
        }

        log.error("Category doesn't belong to the same user!!");
        return false;


    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category createNewCategory(AddCategoryForm form) {

        User user = authenticationService.getCurrentAuthenticatedUser();
        List<Category> userCategories = categoryRepository.findByUser(user);

        // Max 8 categories per user
        if (userCategories.size() >= 8) {
            log.info("User already has enough categories");
            throw new RuntimeException("User already has enough categories");
        }

        Category category = new Category();
        category.setCategoryName(form.categoryName());
        category.setUser(user);
        return categoryRepository.save(category);

    }

    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->  new ResourceNotFoundException("Not found!!"));

        if (category.getUser() == null) {
            log.info("Default category, not allowed to exclude");
            throw new ResourceNotFoundException("Not allowed to exclude");
        }

        User user = authenticationService.getCurrentAuthenticatedUser();

        if (!user.getId().equals(category.getUser().getId())) {
            throw new SecurityException("You are not allowed to remove this category");
        }

        categoryRepository.delete(category);
        log.info("Category has been deleted");

    }

    public void editCategory(Long categoryId, AddCategoryForm form) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->  new ResourceNotFoundException("Not found!!"));

        if (category.getUser() == null) {
            log.info("Default category, not allowed to exclude");
            throw new ResourceNotFoundException("Not allowed to exclude");
        }

        category.setCategoryName(form.categoryName());
        categoryRepository.save(category);
        log.info("Category has been edited");




    }
}
