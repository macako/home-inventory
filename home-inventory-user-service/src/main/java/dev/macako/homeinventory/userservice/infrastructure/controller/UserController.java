package dev.macako.homeinventory.userservice.infrastructure.controller;

import dev.macako.homeinventory.userservice.domain.model.User;
import dev.macako.homeinventory.userservice.domain.model.UserNotFoundException;
import dev.macako.homeinventory.userservice.domain.service.UserService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/users")
public class UserController {

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
    Optional<User> user = service.findUserById(id);

    if (user.isEmpty()) throw new UserNotFoundException("id:" + id);

    EntityModel<User> entityModel = EntityModel.of(user.get());

    WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
    entityModel.add(link.withRel("all-users"));

    return entityModel;
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable int id) {
    service.deleteUserById(id);
  }

  @PostMapping("")
  public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    User savedUser = service.saveUser(user);

    URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();

    return created(location).build();
  }

  public List<User> hardcodedResponse(Exception ex) {
    return emptyList();
  }
}
