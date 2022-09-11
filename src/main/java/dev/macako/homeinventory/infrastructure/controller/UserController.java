package dev.macako.homeinventory.infrastructure.controller;

import dev.macako.homeinventory.domain.model.Item;
import dev.macako.homeinventory.domain.model.User;
import dev.macako.homeinventory.domain.model.UserNotFoundException;
import dev.macako.homeinventory.domain.service.UserItemsService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/users")
public class UserController {

  private final UserItemsService service;

  public UserController(UserItemsService service) {
    this.service = service;
  }

  @GetMapping("")
  public List<User> retrieveAllUsers() {
    return service.findAllUsers();
  }

  @GetMapping("/{id}")
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

  @GetMapping("/{id}/items")
  public List<Item> retrieveItemsForUser(@PathVariable int id) {
    Optional<User> user = service.findUserById(id);

    if (user.isEmpty()) throw new UserNotFoundException("id:" + id);

    return user.get().getItems();
  }

  @PostMapping("")
  public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    User savedUser = service.saveUser(user);

    URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();

    return created(location).build();
  }

  @PostMapping("/{id}/items")
  public ResponseEntity<Object> createItemForUser(
      @PathVariable int id, @Valid @RequestBody Item item) {
    Optional<User> user = service.findUserById(id);

    if (user.isEmpty()) throw new UserNotFoundException("id:" + id);

    Item savedItem = service.saveItem(user.get(), item);

    URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedItem.getId()).toUri();

    return created(location).build();
  }
}
