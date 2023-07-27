package rest.recipes.view;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rest.recipes.dto.UserDTO;
import rest.recipes.models.User;
import rest.recipes.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class AuthenticationController {

    private final UserService userService;
    private final ModelMapper mapper;

    @Autowired
    public AuthenticationController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDTO userDTO) {
        if (userService.isExists(userDTO.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            User user = mapper.map(userDTO, User.class);
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
