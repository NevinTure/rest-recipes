package rest.recipes.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rest.recipes.models.Recipe;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("select r from Recipe r left join fetch r.ingredients where r.id = :id")
    Optional<Recipe> findByIdWithIngredients(@Param("id") long id);

    @Query("select r from Recipe r left join fetch r.directions where r.id = :id")
    Optional<Recipe> findByIdWithDirections(@Param("id") long id);

    @Query("select distinct r from Recipe r left join fetch r.ingredients where lower(r.category) = lower(:category)")
    List<Recipe> findByCategoryWithIngredientsIgnoreCase(@Param("category") String category, Sort sort);

    @Query("select distinct r from Recipe r left join fetch r.directions where lower(r.category) = lower(:category)")
    List<Recipe> findByCategoryWithDirectionsIgnoreCase(@Param("category") String category, Sort sort);

    @Query("select distinct r from Recipe r left join fetch r.ingredients where lower(r.name) like lower(concat('%',:name,'%'))")
    List<Recipe> findByNameContainsWithIngredientsIgnoreCase(@Param("name") String name, Sort sort);

    @Query("select distinct r from Recipe r left join fetch r.directions where lower(r.name) like lower(concat('%',:name,'%'))")
    List<Recipe> findByNameContainsWithDirectionsIgnoreCase(@Param("name") String name, Sort sort);

    @Query("select distinct r from Recipe r left join fetch r.directions where r.owner.id = :id")
    List<Recipe> findByUserIdWithDirections(@Param("id") long id);

    @Query("select distinct r from Recipe r left join fetch r.ingredients where r.owner.id = :id")
    List<Recipe> findByUserIdWithIngredients(@Param("id") long id);
}
