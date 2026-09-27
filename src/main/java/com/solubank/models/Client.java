package com.solubank.models;

public class Client {
  private int id;
  private String name;
  private String email;

  public Client(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }
}
