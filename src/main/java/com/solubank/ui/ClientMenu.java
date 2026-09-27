package com.solubank.ui;

import com.solubank.dao.ClientDAO;
import com.solubank.models.Account;
import com.solubank.models.Client;
import com.solubank.services.AccountService;
import com.solubank.services.ClientService;

public class ClientMenu {
  public static void create() {
    String name = Input.readText("Name: ");
    String email = Input.readText("Email: ");
    String t = Input.readText("Account type (1=checking, 2=savings): ");
    Client c = ClientService.addClient(name, email);
    if (c == null) {
      return;
    }
    try {
      Account a;
      if (t.equals("1")) {
        a = AccountService.createCurrent(c.getId());
      } else {
        double rate = Input.readDouble("Rate (ex 2.5): ");
        a = AccountService.createSavings(c.getId(), rate);
      }
      if (a == null) {
        ClientDAO.delete(c.getId());
        System.out.println("Account not created. Client removed.");
      }
    } catch (Exception e) {
      ClientDAO.delete(c.getId());
      System.out.println("Account not created (" + e.getMessage() + "). Client removed.");
    }
  }
}
