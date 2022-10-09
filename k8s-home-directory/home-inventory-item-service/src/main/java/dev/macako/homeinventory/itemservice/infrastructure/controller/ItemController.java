package dev.macako.homeinventory.itemservice.infrastructure.controller;

import dev.macako.homeinventory.itemservice.domain.model.Item;
import dev.macako.homeinventory.itemservice.domain.model.User;
import dev.macako.homeinventory.itemservice.domain.model.UserItems;
import dev.macako.homeinventory.itemservice.domain.model.UserNotFoundException;
import dev.macako.homeinventory.itemservice.domain.service.ItemService;
import dev.macako.homeinventory.itemservice.domain.service.UserService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
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

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.hateoas.EntityModel.of;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/items")
public class ItemController {

  public static final String USER_NOT_FOUND = "USER_NOT_FOUND id: {}";
  public static final String ITEM_NOT_FOUND = "ITEM_NOT_FOUND id: {}";

  private final Logger logger = getLogger(ItemController.class);

  private final ItemService itemService;
  private final UserService userService;

  public ItemController(ItemService itemService, UserService userService) {
    this.itemService = itemService;
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public EntityModel<Item> retrieveItemById(@PathVariable int id) {
    validateItemById(id);

    final Optional<Item> item = itemService.findItemById(id);
    final EntityModel<Item> entityModel = of(item.get());
    final WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllItems());

    entityModel.add(link.withRel("all-items"));

    return entityModel;
  }

  @DeleteMapping("/{id}")
  public void deleteItem(@PathVariable int id) {
    validateItemById(id);

    itemService.deleteItemById(id);
  }

  @DeleteMapping("/user/{id}")
  public void deleteItemsByUserId(@PathVariable int id) {
    validateUserById(id);

    itemService.deleteAllItemsByUserId(id);
  }

  @Bulkhead(name = "sample-api")
  @RateLimiter(name = "default")
  @GetMapping("")
  public List<Item> retrieveAllItems() {
    return itemService.findAllItems();
  }

  @Bulkhead(name = "sample-api")
  @GetMapping("/user/{id}")
  public List<Item> retrieveItemsByUser(@PathVariable int id) {
    validateUserById(id);

    return itemService.findItemsByUserId(id);
  }

  @PostMapping("/user/{id}")
  public ResponseEntity<Object> createItemForUser(
      @PathVariable int id, @Valid @RequestBody Item item) {
    validateUserById(id);

    final Optional<UserItems> user = itemService.findUserById(id);
    final Item savedItem = itemService.saveItem(user.get(), item);
    final URI location =
        fromCurrentRequest().path("/{id}").buildAndExpand(savedItem.getId()).toUri();

    return created(location).build();
  }

  private void validateUserById(int id) {
    final Optional<User> user = userService.findUserById(id);

    if (user.isEmpty()) {
      logger.error(USER_NOT_FOUND, id);
      throw new UserNotFoundException("user id not found");
    }
  }

  private void validateItemById(int id) {
    final Optional<Item> item = itemService.findItemById(id);

    if (item.isEmpty()) {
      logger.error(ITEM_NOT_FOUND, id);
      throw new UserNotFoundException("item id not found");
    }
  }
}
