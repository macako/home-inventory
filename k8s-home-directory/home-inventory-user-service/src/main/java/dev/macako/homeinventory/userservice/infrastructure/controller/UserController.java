package dev.macako.homeinventory.userservice.infrastructure.controller;

import dev.macako.homeinventory.userservice.domain.model.Item;
import dev.macako.homeinventory.userservice.domain.model.User;
import dev.macako.homeinventory.userservice.domain.model.UserNotFoundException;
import dev.macako.homeinventory.userservice.domain.service.UserService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.hateoas.EntityModel.of;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/users")
public class UserController {
  public static final String USER_NOT_FOUND = "USER_NOT_FOUND id: {}";
  private final Logger logger = getLogger(UserController.class);

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping("")
  @Bulkhead(name = "sample-api")
  @RateLimiter(name = "default", fallbackMethod = "hardcodedResponse")
  public List<User> retrieveAllUsers() {
    return service.findAllUsers();
  }

  @GetMapping("/{id}")
  @Bulkhead(name = "sample-api")
  @CircuitBreaker(name = "default")
  public EntityModel<User> retrieveUser(@PathVariable int id) {
    validateUserById(id);

    final Optional<User> user = service.findUserById(id);
    final EntityModel<User> entityModel = of(user.get());
    final WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());

    entityModel.add(link.withRel("all-users"));

    return entityModel;
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable int id) {
    validateUserById(id);

    service.deleteUserById(id);
  }

  @PostMapping("")
  public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    final User savedUser = service.saveUser(user);
    final URI location =
        fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();

    return created(location).build();
  }

  @GetMapping("/{id}/items")
  public List<Item> retrieveItemsByUser(@PathVariable int id) {
    validateUserById(id);

    return service.findItemsByUserId(id);
  }

  public List<User> hardcodedResponse(Exception ex) {
    return emptyList();
  }

  private void validateUserById(int id) {
    final Optional<User> user = service.findUserById(id);

    if (user.isEmpty()) {
      logger.error(USER_NOT_FOUND, id);
      throw new UserNotFoundException("user id not found");
    }
  }
}
