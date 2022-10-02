package dev.macako.homeinventory.itemservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.macako.homeinventory.itemservice.domain.model.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Item {
  @Id @GeneratedValue private Integer id;

  @Size(min = 10)
  private String description;

  @ManyToOne(fetch = LAZY)
  @JsonIgnore
  private User user;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "Item [id=" + id + ", description=" + description + "]";
  }
}
