package dev.macako.homeinventory.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "user_details")
public class User {

  @Id @GeneratedValue private Integer id;

  @Size(min = 2, message = "Name should have at least 2 characters")
  // @JsonProperty("user_name")
  private String name;

  @Past(message = "Birth Date should be in the past")
  // @JsonProperty("birth_date")
  private LocalDate birthDate;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<Item> items;

  protected User() {}

  public User(Integer id, String name, LocalDate birthDate) {
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
