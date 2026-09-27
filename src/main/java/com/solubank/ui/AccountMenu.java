package com.solubank.ui;

import com.solubank.services.AccountService;

public class AccountMenu {
  public static void create() {
    int id = Input.readInt("Client id: ");
    createForClient(id);
  }

  public static void createForClient(int id) {
    String t = Input.readText("Type (1=checking, 2=savings): ");
    switch (t) {
      case "1" -> AccountService.createCurrent(id);
      default -> {
        double rate = Input.readDouble("Rate (ex 2.5): ");
        AccountService.createSavings(id, rate);
      }
    }
  }
}
