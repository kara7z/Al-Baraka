package com.solubank.ui;

import com.solubank.models.Transaction;
import com.solubank.services.TransactionService;
import java.util.List;

public class TransactionMenu {
  public static void deposit() {
    String num = Input.readText("Account number: ");
    double amount = Input.readDouble("Amount: ");
    String place = Input.readText("Place: ");
    TransactionService.deposit(num, amount, place);
  }

  public static void withdraw() {
    String num = Input.readText("Account number: ");
    double amount = Input.readDouble("Amount: ");
    String place = Input.readText("Place: ");
    TransactionService.withdraw(num, amount, place);
  }

  public static void transfer() {
    String from = Input.readText("Source account: ");
    String to = Input.readText("Destination account: ");
    double amount = Input.readDouble("Amount: ");
    String place = Input.readText("Place: ");
    TransactionService.transfer(from, to, amount, place);
  }

  public static void history() {
    String num = Input.readText("Account number: ");
    List<Transaction> list = TransactionService.historyByAccount(num);
    if (list.size() == 0) {
      System.out.println("No operations.");
      return;
    }
    for (int i = 0; i < list.size(); i++) {
      Transaction t = list.get(i);
      System.out.println(t.getDate() + " | " + t.getType() + " | " + t.getBalance() + " | " + t.getPlace());
    }
  }
}
