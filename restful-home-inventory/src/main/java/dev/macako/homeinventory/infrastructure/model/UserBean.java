package dev.macako.homeinventory.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.macako.homeinventory.domain.model.Item;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class UserBean {

  private Integer id;

  @Size(min = 2, message = "Name should have at least 2 characters")
  private String name;

  @Past(message = "Birth Date should be in the past")
  private LocalDate birthDate;

  @JsonIgnore private List<Item> items;

  protected UserBean() {}

  public UserBean(Integer id, String name, LocalDate birthDate) {
    super();
    this.id = id;
    this.name = name;
    this.birthDate = birthDate;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setPosts(List<Item> items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + ", birthDate=" + birthDate + "]";
  }
}
