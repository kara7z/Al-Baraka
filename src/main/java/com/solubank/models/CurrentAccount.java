package com.solubank.models;

public class CurrentAccount extends Account {

  private double overdraftLimit = 5000;

  public CurrentAccount(int clientId) {
    super(clientId);
  }

  public CurrentAccount(int clientId, double balance) {
    super(clientId, balance);
  }

  public double getOverdraftLimit() {
    return overdraftLimit;
  }

  public void setOverdraftLimit(double overdraftLimit) {
    this.overdraftLimit = overdraftLimit;
  }
}
