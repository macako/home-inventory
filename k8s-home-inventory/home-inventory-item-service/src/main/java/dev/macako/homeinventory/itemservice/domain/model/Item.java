package dev.macako.homeinventory.itemservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Item {

  @Id @GeneratedValue private Integer id;

  @Size(min = 5, message = "description should have at least 5 characters")
  private String description;

  @ManyToOne(fetch = LAZY)
  @JsonIgnore
  private UserItems userItems;

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

  public UserItems getUserItems() {
    return userItems;
  }

  public void setUserItems(UserItems userItems) {
    this.userItems = userItems;
  }

  @Override
  public String toString() {
    return "Item [id=" + id + ", description=" + description + "]";
  }
}
