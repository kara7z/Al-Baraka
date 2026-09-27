package com.solubank.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
  private DBConnection() {
  }

  public static Connection getConnection() {
    var url = System.getProperty("db.url", System.getenv().getOrDefault("DB_URL",
        "jdbc:mariadb://localhost:3306/albaraka"));
    var user = System.getProperty("db.user", System.getenv().getOrDefault("DB_USER", "root"));
    var password = System.getProperty("db.password",
        System.getenv().getOrDefault("DB_PASSWORD", "Admin1234@"));
    try {
      return DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      throw new DbException("Connexion DB echouee (" + url + "): " + e.getMessage(), e);
    }
  }
}
