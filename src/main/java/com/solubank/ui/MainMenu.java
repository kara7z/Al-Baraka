package com.solubank.ui;

public class MainMenu {
  public static void start() {
    boolean run = true;
    while (run) {
      print();
      String choix = Input.readText("Your choice: ");
      try {
        switch (choix) {
          case "1" -> ClientMenu.create();
          case "2" -> AccountMenu.create();
          case "3" -> TransactionMenu.deposit();
          case "4" -> TransactionMenu.withdraw();
          case "5" -> TransactionMenu.transfer();
          case "6" -> TransactionMenu.history();
          case "7" -> RapportMenu.analyse();
          case "8" -> RapportMenu.alertes();
          case "0" -> {
            run = false;
            System.out.println("Goodbye.");
          }
          default -> System.out.println("Invalid choice, enter 0-8.");
        }
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }
    }
  }

  static void print() {
    System.out.println("");
    System.out.println("=== Al Baraka Bank ===");
    System.out.println("1. Create client");
    System.out.println("2. Create account");
    System.out.println("3. Deposit");
    System.out.println("4. Withdraw");
    System.out.println("5. Transfer");
    System.out.println("6. Account history");
    System.out.println("7. Analysis (Top5 / month / suspects / inactive)");
    System.out.println("8. Alerts (low balance / inactivity)");
    System.out.println("0. Quit");
  }
}
