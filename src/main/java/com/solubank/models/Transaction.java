package com.solubank.models;

import java.time.LocalDate;

import com.solubank.enums.TransactionType;

public class Transaction {
  private int id;
  private LocalDate date;
  private double balance;
  private TransactionType type;
  private String place;
  private String accountId;

  public Transaction(double balance, TransactionType type, String place, String accountId) {
    this.balance = balance;
    this.type = type;
    this.place = place;
    this.accountId = accountId;
    this.date = LocalDate.now();
  }

  public double getBalance() {
    return balance;
  }

  public LocalDate getDate() {
    return date;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public TransactionType getType() {
    return type;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public void setType(TransactionType type) {
    this.type = type;
  }

  public String getPlace() {
    return place;
  }

  public String getAccountId() {
    return accountId;
  }
}
