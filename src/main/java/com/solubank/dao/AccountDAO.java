package com.solubank.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.solubank.models.Account;
import com.solubank.models.CurrentAccount;
import com.solubank.models.SavingsAccount;
import com.solubank.utils.DBConnection;
import com.solubank.utils.DbException;

public class AccountDAO {
  private static Account rowToAccount(ResultSet r) throws Exception {
    String id = r.getString("id");
    String number = r.getString("number");
    double balance = r.getDouble("balance");
    int customerId = r.getInt("customerId");
    String type = r.getString("accountType");
    return switch (type) {
      case "checking" -> {
        CurrentAccount c = new CurrentAccount(customerId, balance);
        c.setId(id);
        c.setNumber(number);
        c.setOverdraftLimit(r.getDouble("overdraft_limit"));
        yield c;
      }
      default -> {
        SavingsAccount c = new SavingsAccount(customerId, balance);
        c.setId(id);
        c.setNumber(number);
        c.setInterestRate(r.getDouble("interest_rate"));
        yield c;
      }
    };
  }

  public static void AddAccount(Account account) {
    String sql = "INSERT INTO Account (number, balance, customerId, accountType, overdraft_limit, interest_rate) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, account.getNumber());
      stmt.setDouble(2, account.getBalance());
      stmt.setInt(3, account.getClientId());
      if (account instanceof CurrentAccount) {
        CurrentAccount c = (CurrentAccount) account;
        stmt.setString(4, "checking");
        stmt.setDouble(5, c.getOverdraftLimit());
        stmt.setDouble(6, 0);
      } else if (account instanceof SavingsAccount) {
        SavingsAccount c = (SavingsAccount) account;
        stmt.setString(4, "savings");
        stmt.setDouble(5, 0);
        stmt.setDouble(6, c.getInterestRate());
      } else {
        stmt.setString(4, "checking");
        stmt.setDouble(5, 0);
        stmt.setDouble(6, 0);
      }
      stmt.executeUpdate();
      Account back = findByNumber(account.getNumber());
      if (back != null) {
        account.setId(back.getId());
      }
      System.out.println("Account added successfully");
    } catch (Exception e) {
      throw new DbException("Cannot add account: " + e.getMessage(), e);
    }
  }

  static public boolean isExistNumber(String number) {
    String sql = "SELECT count(*) FROM Account where number = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, number);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1) > 0;
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot check number: " + e.getMessage(), e);
    }
    return false;
  }

  public static Account findById(String id) {
    String sql = "SELECT * FROM Account WHERE id = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, id);
      try (ResultSet r = stmt.executeQuery()) {
        if (r.next()) {
          return rowToAccount(r);
        }
        return null;
      }
    } catch (Exception e) {
      throw new DbException("Cannot find account: " + e.getMessage(), e);
    }
  }

  public static Account findByNumber(String number) {
    String sql = "SELECT * FROM Account WHERE number = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, number);
      try (ResultSet r = stmt.executeQuery()) {
        if (r.next()) {
          return rowToAccount(r);
        }
        return null;
      }
    } catch (Exception e) {
      throw new DbException("Cannot search by number: " + e.getMessage(), e);
    }
  }

  public static List<Account> findByClient(int customerId) {
    List<Account> list = new ArrayList<Account>();
    String sql = "SELECT * FROM Account WHERE customerId = ? ORDER BY number";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, customerId);
      try (ResultSet r = stmt.executeQuery()) {
        while (r.next()) {
          list.add(rowToAccount(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot find client accounts: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Account> findAll() {
    List<Account> list = new ArrayList<Account>();
    String sql = "SELECT * FROM Account ORDER BY number";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet r = stmt.executeQuery()) {
      while (r.next()) {
        list.add(rowToAccount(r));
      }
    } catch (Exception e) {
      throw new DbException("Cannot list accounts: " + e.getMessage(), e);
    }
    return list;
  }

  public static boolean updateBalance(String id, double balance) {
    String sql = "UPDATE Account SET balance = ? WHERE id = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setDouble(1, balance);
      stmt.setString(2, id);
      return stmt.executeUpdate() > 0;
    } catch (Exception e) {
      throw new DbException("Cannot update balance: " + e.getMessage(), e);
    }
  }

  public static boolean updateOverdraft(String id, double overdraft) {
    String sql = "UPDATE Account SET overdraft_limit = ? WHERE id = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setDouble(1, overdraft);
      stmt.setString(2, id);
      return stmt.executeUpdate() > 0;
    } catch (Exception e) {
      throw new DbException("Cannot update overdraft: " + e.getMessage(), e);
    }
  }

  public static boolean updateRate(String id, double rate) {
    String sql = "UPDATE Account SET interest_rate = ? WHERE id = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setDouble(1, rate);
      stmt.setString(2, id);
      return stmt.executeUpdate() > 0;
    } catch (Exception e) {
      throw new DbException("Cannot update rate: " + e.getMessage(), e);
    }
  }

  public static boolean delete(String id) {
    String sql = "DELETE FROM Account WHERE id = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, id);
      return stmt.executeUpdate() > 0;
    } catch (Exception e) {
      throw new DbException("Cannot delete account: " + e.getMessage(), e);
    }
  }

  public static double balanceTotalByClient(int customerId) {
    String sql = "SELECT COALESCE(SUM(balance),0) FROM Account WHERE customerId = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, customerId);
      try (ResultSet r = stmt.executeQuery()) {
        if (r.next()) {
          return r.getDouble(1);
        }
        return 0;
      }
    } catch (Exception e) {
      throw new DbException("Cannot compute total balance: " + e.getMessage(), e);
    }
  }

  public static int countByClient(int customerId) {
    String sql = "SELECT COUNT(*) FROM Account WHERE customerId = ?";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, customerId);
      try (ResultSet r = stmt.executeQuery()) {
        if (r.next()) {
          return r.getInt(1);
        }
        return 0;
      }
    } catch (Exception e) {
      throw new DbException("Cannot count accounts: " + e.getMessage(), e);
    }
  }

  public static Account findMaxAccount() {
    String sql = "SELECT * FROM Account ORDER BY balance DESC LIMIT 1";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet r = stmt.executeQuery()) {
      if (r.next()) {
        return rowToAccount(r);
      }
      return null;
    } catch (Exception e) {
      throw new DbException("Cannot find max balance: " + e.getMessage(), e);
    }
  }

  public static Account findMinAccount() {
    String sql = "SELECT * FROM Account ORDER BY balance ASC LIMIT 1";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet r = stmt.executeQuery()) {
      if (r.next()) {
        return rowToAccount(r);
      }
      return null;
    } catch (Exception e) {
      throw new DbException("Cannot find min balance: " + e.getMessage(), e);
    }
  }

  public static List<Account> findLowBalance(double threshold) {
    List<Account> list = new ArrayList<Account>();
    String sql = "SELECT * FROM Account WHERE balance < ? ORDER BY balance";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setDouble(1, threshold);
      try (ResultSet r = stmt.executeQuery()) {
        while (r.next()) {
          list.add(rowToAccount(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot find low balances: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Account> findInactiveSince(LocalDate cutoff) {
    List<Account> list = new ArrayList<Account>();
    String sql = "SELECT * FROM Account cp WHERE NOT EXISTS ("
        + "SELECT 1 FROM Transaction t WHERE t.accountId = cp.id AND t.date >= ?)";
    try (Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setDate(1, Date.valueOf(cutoff));
      try (ResultSet r = stmt.executeQuery()) {
        while (r.next()) {
          list.add(rowToAccount(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot find inactive accounts: " + e.getMessage(), e);
    }
    return list;
  }
}
