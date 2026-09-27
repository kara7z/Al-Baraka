package com.solubank.models;

public class SavingsAccount extends Account {
  protected double interestRate = 0;

  public SavingsAccount(int clientId) {
    super(clientId);
  }

  public SavingsAccount(int clientId, double balance) {
    super(clientId, balance);
  }

  public double getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(double interestRate) {
    this.interestRate = interestRate;
  }
}
