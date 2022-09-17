package dev.macako.homeinventory.itemservice.infrastructure.controller;

import dev.macako.homeinventory.itemservice.domain.model.Item;
import dev.macako.homeinventory.itemservice.domain.model.User;
import dev.macako.homeinventory.itemservice.domain.model.UserNotFoundException;
import dev.macako.homeinventory.itemservice.domain.service.ItemService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.ResponseEntity;
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
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/items")
public class ItemController {

  private final ItemService service;

  public ItemController(ItemService service) {
    this.service = service;
  }

  @GetMapping("")
  @Bulkhead(name = "sample-api")
  @RateLimiter(name = "default")
  public List<Item> retrieveAllItems() {
    return service.findAllItems();
  }

  @GetMapping("/{id}")
  @Bulkhead(name = "sample-api")
  @CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
  public List<Item> retrieveItemsForUser(@PathVariable int id) {
    final Optional<User> user = service.findUserById(id);

    if (user.isEmpty()) {
      throw new UserNotFoundException("id:" + id);
    }

    return service.findItemsByUserId(id);
  }

  @PostMapping("/{id}")
  public ResponseEntity<Object> createItemForUser(
      @PathVariable int id, @Valid @RequestBody Item item) {
    final Optional<User> user = service.findUserById(id);

    if (user.isEmpty()) {
      throw new UserNotFoundException("id:" + id);
    }

    final Item savedItem = service.saveItem(user.get(), item);
    final URI location =
        fromCurrentRequest().path("/{id}").buildAndExpand(savedItem.getId()).toUri();

    return created(location).build();
  }

  public List<Item> hardcodedResponse(Exception ex) {
    return emptyList();
  }
}
