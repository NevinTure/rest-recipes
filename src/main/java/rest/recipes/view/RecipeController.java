package rest.recipes.view;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rest.recipes.dto.RecipeDTO;
import rest.recipes.models.Recipe;
import rest.recipes.models.User;
import rest.recipes.services.RecipeService;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipe")
@Validated
public class RecipeController {

    private final RecipeService recipeService;
    private final ModelMapper mapper;

    @Autowired
    public RecipeController(RecipeService recipeService, ModelMapper mapper) {
        this.recipeService = recipeService;
        this.mapper = mapper;
    }


    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable(name = "id") long id) {
        Optional<Recipe> recipeOptional = recipeService.get(id);
        if (recipeOptional.isPresent()) {
            RecipeDTO recipeDTO = mapper.map(recipeOptional.get(), RecipeDTO.class);
            return new ResponseEntity<>(recipeDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> addRecipe(@Valid @RequestBody RecipeDTO receivedRecipe) {
        User user = getCurrentUser();
        Recipe recipe = mapper.map(receivedRecipe, Recipe.class);
        recipe.setOwner(user);
        recipeService.save(recipe);
        return new ResponseEntity<>(Map.of("id", recipe.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRecipe(@PathVariable(name = "id") long id) {
        Optional<Recipe> recipeOptional = recipeService.get(id);
        if(recipeOptional.isPresent()) {
            User user = getCurrentUser();
            List<Recipe> recipes = recipeService.findByUserId(user.getId());
            if (recipes.contains(recipeOptional.get())) {
                recipeService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRecipe(@PathVariable(name = "id") long id, @Valid @RequestBody RecipeDTO recipeDTO) {
        Optional<Recipe> recipeOptional = recipeService.get(id);
        if(recipeOptional.isPresent()) {
            User user = getCurrentUser();
            List<Recipe> recipes = recipeService.findByUserId(user.getId());
            if (recipes.contains(recipeOptional.get())) {
                Recipe recipe = mapper.map(recipeDTO, Recipe.class);
                recipe.setId(id);
                recipe.setOwner(user);
                recipeService.update(recipe);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchRecipes(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "name", required = false) String name) {

        if(category == null && name == null || category != null && name != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<RecipeDTO> recipeDTOs;
            if (category != null) {
                recipeDTOs = recipeService
                        .findByCategory(category)
                        .stream()
                        .map(v -> mapper.map(v, RecipeDTO.class))
                        .collect(Collectors.toList());
            } else {
                recipeDTOs = recipeService
                        .findByNameContains(name)
                        .stream()
                        .map(v -> mapper.map(v, RecipeDTO.class))
                        .collect(Collectors.toList());
            }
            return new ResponseEntity<>(recipeDTOs, HttpStatus.OK);
        }
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
