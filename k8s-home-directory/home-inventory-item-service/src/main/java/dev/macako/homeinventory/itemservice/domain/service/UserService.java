package dev.macako.homeinventory.itemservice.domain.service;

import dev.macako.homeinventory.itemservice.domain.model.User;
import dev.macako.homeinventory.itemservice.domain.model.UserNotFoundException;
import dev.macako.homeinventory.itemservice.domain.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

  private final Logger logger = getLogger(UserService.class);
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Retry(name = "default", fallbackMethod = "hardcodedResponse")
  @CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
  public Optional<User> findUserById(int id) {
    return userRepository.findById(id);
  }

  public Optional<User> hardcodedResponse(HttpClientErrorException httpClientErrorException) {
    logger.error("ERROR_GETTING_USER", httpClientErrorException);

    if (NOT_FOUND.equals(httpClientErrorException.getStatusCode())) {
      logger.error("USER_NOT_FOUND", httpClientErrorException);
      throw new UserNotFoundException("user id not found");
    }

    return empty();
  }
}
