package dev.macako.homeinventory.userservice.infrastructure.controller;

import dev.macako.homeinventory.userservice.domain.model.User;
import dev.macako.homeinventory.userservice.domain.model.UserNotFoundException;
import dev.macako.homeinventory.userservice.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.time.LocalDate.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
  public static final int USER_ID = 1;
  public static final User USER1 = new User(USER_ID, "name1", now());
  public static final User USER2 = new User(2, "name2", now());
  public static final Optional<User> EMPTY_USER = empty();
  @Mock private UserService service;

  @InjectMocks private UserController controller;

  @Test
  void retrieveAllUsers() {
    when(service.findAllUsers()).thenReturn(newArrayList(USER1, USER2));

    List<User> users = controller.retrieveAllUsers();

    assertThat(users, hasSize(2));
  }

  @Test()
  void shouldRetrieveUser() {
    when(service.findUserById(USER_ID)).thenReturn(of(USER1));

    User user1 = controller.retrieveUser(USER_ID).getContent();

    assertThat(user1, hasProperty("name", equalTo("name1")));
    assertThat(user1, hasToString(format("User [id=1, name=name1, birthDate=%s]", now())));
  }

  @Test()
  void shouldNotRetrieveUser() {
    when(service.findUserById(USER_ID)).thenReturn(EMPTY_USER);

    UserNotFoundException thrown =
        assertThrows(UserNotFoundException.class, () -> controller.retrieveUser(USER_ID));

    assertThat(thrown.getMessage(), equalTo("user id not found"));
  }

  @Test
  void shouldDeleteUser() {
    when(service.findUserById(USER_ID)).thenReturn(of(USER1));

    controller.deleteUser(USER_ID);

    verify(service).deleteUserById(USER_ID);
  }

  @Test
  void shouldNotDeleteUser() {
    when(service.findUserById(USER_ID)).thenReturn(EMPTY_USER);

    UserNotFoundException thrown =
        assertThrows(UserNotFoundException.class, () -> controller.deleteUser(USER_ID));

    assertThat(thrown.getMessage(), equalTo("user id not found"));
  }

  @Test
  void shouldDisplayUriForCreateUser() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(service.saveUser(any(User.class))).thenReturn(USER1);

    ResponseEntity<User> user = controller.createUser(USER1);

    assertThat(user.getHeaders().getLocation(), hasToString("http://localhost/1"));
  }

  @Test
  void shouldRetrieveItemsByUser() {
    when(service.findUserById(USER_ID)).thenReturn(EMPTY_USER);

    UserNotFoundException thrown =
        assertThrows(UserNotFoundException.class, () -> controller.retrieveItemsByUser(USER_ID));

    assertThat(thrown.getMessage(), equalTo("user id not found"));
  }

  @Test
  void shouldNotRetrieveItemsByUser() {
    when(service.findUserById(USER_ID)).thenReturn(of(USER1));

    controller.retrieveItemsByUser(USER_ID);

    verify(service).findItemsByUserId(USER_ID);
  }
}
