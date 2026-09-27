package com.solubank;

import com.solubank.ui.MainMenu;
import com.solubank.utils.DBConnection;

public class Main {
  public static void main(String[] args) {
    try {
      DBConnection.getConnection().close();
      System.out.println("Connected to database.");
    } catch (Exception e) {
      System.out.println("Database not connected: " + e.getMessage());
      System.out.println("Check MariaDB albaraka then restart.");
    }
    MainMenu.start();
  }
}
