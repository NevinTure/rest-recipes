package rest.recipes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class RecipeDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Size(min = 1)
    private List<String> ingredients;

    @NotNull
    @Size(min = 1)
    private List<String> directions;

    @NotBlank
    private String category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime date;

}
