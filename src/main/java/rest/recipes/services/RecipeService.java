package rest.recipes.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rest.recipes.models.Recipe;
import rest.recipes.repositories.RecipeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void save(Recipe recipe) {
        recipe.setDate(LocalDateTime.now());
        recipeRepository.save(recipe);
    }

    public Optional<Recipe> get(long id) {

        final Optional<Recipe> recipeOptional = recipeRepository.findByIdWithIngredients(id);

        return recipeOptional.isPresent() ? recipeRepository.findByIdWithDirections(id) : recipeOptional;
    }

    public void delete(long id) {
        recipeRepository.deleteById(id);
    }

    public void update(Recipe recipe) {
        save(recipe);
    }

    public List<Recipe> findByCategory(String category) {
        final List<Recipe> recipes = recipeRepository.findByCategoryWithIngredientsIgnoreCase(
                category,
                Sort.by("date").descending()
        );

        return !recipes.isEmpty() ? recipeRepository.findByCategoryWithDirectionsIgnoreCase(
                category,
                Sort.by("date").descending()
        ) : recipes;
    }

    public List<Recipe> findByNameContains(String contains) {
        final List<Recipe> recipes = recipeRepository.findByNameContainsWithIngredientsIgnoreCase(
                contains,
                Sort.by("date").descending()
        );

        return !recipes.isEmpty() ? recipeRepository.findByNameContainsWithDirectionsIgnoreCase(
                contains,
                Sort.by("date").descending()
        ) : recipes;
    }

    public List<Recipe> findByUserId(long id) {
        final List<Recipe> recipes = recipeRepository.findByUserIdWithIngredients(id);

        return !recipes.isEmpty() ? recipeRepository.findByUserIdWithDirections(id) : recipes;
    }
}
