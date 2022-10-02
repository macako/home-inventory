package dev.macako.homeinventory.itemservice.infrastructure.repository;

import dev.macako.homeinventory.itemservice.domain.model.User;
import dev.macako.homeinventory.itemservice.domain.repository.UserRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Repository
public class UserHttpClientRepository implements UserRepository {
  private static final HttpHeaders httpHeaders;

  static {
    httpHeaders = new HttpHeaders();
    String auth = "username:password";
    byte[] encodedAuth = encodeBase64(auth.getBytes(UTF_8));
    String authHeader = "Basic " + new String(encodedAuth);

    httpHeaders.set(AUTHORIZATION, authHeader);
  }

  private final Logger logger = getLogger(UserHttpClientRepository.class);
  private final RestTemplate restTemplate = new RestTemplate();
  private final String userByIdUri;

  public UserHttpClientRepository(
      @Value("${http.homeInventoryUserService.userByIdUri}") String userByIdUri) {
    this.userByIdUri = userByIdUri;
  }

  @Override
  @Retry(name = "default", fallbackMethod = "hardcodedResponse")
  public Optional<User> findById(int id) {
    final String url = format(userByIdUri, id);

    logger.info("GETTING_USER");

    try {
      final ResponseEntity<User> userResponseEntity =
          restTemplate.exchange(url, GET, new HttpEntity<User>(httpHeaders), User.class);

      return of(userResponseEntity.getBody());
    } catch (HttpClientErrorException httpClientErrorException) {
      if (NOT_FOUND.equals(httpClientErrorException.getStatusCode())) {
        logger.error("USER_NOT_FOUND", httpClientErrorException);
        return empty();
      }
      throw httpClientErrorException;
    }
  }

  private Optional<User> hardcodedResponse(RestClientException restClientException) {
    logger.error("ERROR_GETTING_USER", restClientException);
    return empty();
  }
}
