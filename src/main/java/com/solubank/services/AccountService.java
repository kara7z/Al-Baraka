package com.solubank.services;

import com.solubank.dao.AccountDAO;
import com.solubank.dao.ClientDAO;
import com.solubank.models.Account;
import com.solubank.models.CurrentAccount;
import com.solubank.models.SavingsAccount;
import java.util.List;

public class AccountService {

  public static Account createCurrent(int clientId) {
    if (ClientDAO.findById(clientId) == null) {
      System.out.println("Client not found.");
      return null;
    }
    CurrentAccount c = new CurrentAccount(clientId);
    AccountDAO.AddAccount(c);
    System.out.println("Checking account created, number = " + c.getNumber());
    return c;
  }

  public static Account createSavings(int clientId, double rate) {
    if (ClientDAO.findById(clientId) == null) {
      System.out.println("Client not found.");
      return null;
    }
    SavingsAccount c = new SavingsAccount(clientId);
    c.setInterestRate(rate);
    AccountDAO.AddAccount(c);
    System.out.println("Savings account created, number = " + c.getNumber());
    return c;
  }

  public static List<Account> accountsOfClient(int clientId) {
    return AccountDAO.findByClient(clientId);
  }

  public static Account searchByNumber(String number) {
    return AccountDAO.findByNumber(number);
  }

  public static Account maxBalance() {
    return AccountDAO.findMaxAccount();
  }

  public static Account minBalance() {
    return AccountDAO.findMinAccount();
  }

  public static List<Account> listAll() {
    return AccountDAO.findAll();
  }

  public static List<Account> lowBalance(double threshold) {
    return AccountDAO.findLowBalance(threshold);
  }
}
