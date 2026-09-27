package com.solubank.models;

import java.util.Random;

public abstract class Account {
  protected String id;
  protected String number;
  protected double balance = 0;
  protected int clientId;

  public Account(int clientId) {
    this.clientId = clientId;
    this.number = getAccountNumber();
  }

  public Account(int clientId, double balance) {
    this.clientId = clientId;
    this.balance = balance;
    this.number = getAccountNumber();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public int getClientId() {
    return clientId;
  }

  public static String getAccountNumber() {
    Random random = new Random();
    StringBuilder accountNumber = new StringBuilder();

    for (int i = 0; i < 24; i++) {
      accountNumber.append(random.nextInt(10));
    }
    return accountNumber.toString();
  }
}
