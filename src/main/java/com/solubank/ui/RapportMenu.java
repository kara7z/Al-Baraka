package com.solubank.ui;

import com.solubank.models.Account;
import com.solubank.models.Transaction;
import com.solubank.services.RapportService;
import com.solubank.services.TransactionService;
import java.util.List;

public class RapportMenu {
  public static void analyse() {
    System.out.println("1-Top5  2-Monthly report  3-Group by type  4-Suspects  5-Inactive");
    String c = Input.readText("Choice: ");
    switch (c) {
      case "1" -> RapportService.printTop5();
      case "2" -> {
        int year = Input.readInt("Year (ex 2026): ");
        int month = Input.readInt("Month (1-12): ");
        RapportService.printMonthlyReport(year, month);
      }
      case "3" -> TransactionService.printGroupByType();
      case "4" -> {
        List<Transaction> suspects = RapportService.suspectAmount();
        System.out.println("Suspects amount>10000: " + suspects.size());
        for (int i = 0; i < suspects.size(); i++) {
          System.out.println(" tx " + suspects.get(i).getId() + " " + suspects.get(i).getBalance());
        }
      }
      case "5" -> {
        List<Account> inactive = RapportService.inactiveAccounts(90);
        System.out.println("Inactive: " + inactive.size());
        for (int i = 0; i < inactive.size(); i++) {
          System.out.println(" " + inactive.get(i).getNumber() + " balance=" + inactive.get(i).getBalance());
        }
      }
      default -> System.out.println("Invalid choice.");
    }
  }

  public static void alertes() {
    RapportService.printAlerts();
  }
}
