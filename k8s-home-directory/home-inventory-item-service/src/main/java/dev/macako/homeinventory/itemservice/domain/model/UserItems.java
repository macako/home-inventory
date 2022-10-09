package dev.macako.homeinventory.itemservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "user_items")
public class UserItems {
  @Id @GeneratedValue private Integer id;

  @OneToMany(mappedBy = "userItems", cascade = ALL)
  @JsonIgnore
  private List<Item> items;

  protected UserItems() {}

  public UserItems(Integer id) {
    super();
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "User [id=" + id + "]";
  }
}
