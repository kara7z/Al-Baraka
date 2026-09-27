package com.solubank.dao;

import com.solubank.enums.TransactionType;
import com.solubank.dto.MonthlyStats;
import com.solubank.models.Transaction;
import com.solubank.utils.DBConnection;
import com.solubank.utils.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TransactionDAO {

  private static String toDb(TransactionType type) {
    return switch (type) {
      case WITHDRAW -> "WITHDRAWAL";
      default -> type.name();
    };
  }

  private static TransactionType fromDb(String t) {
    if (t.equals("WITHDRAWAL")) {
      return TransactionType.WITHDRAW;
    }
    return TransactionType.valueOf(t);
  }

  private static Transaction rowToTx(ResultSet r) throws Exception {
    Transaction tx = new Transaction(
        r.getDouble("amount"),
        fromDb(r.getString("type")),
        r.getString("location"),
        r.getString("accountId"));
    tx.setId(r.getInt("id"));
    tx.setDate(r.getTimestamp("date").toLocalDateTime().toLocalDate());
    return tx;
  }

  public static int create(Transaction tx) {
    String sql = "INSERT INTO Transaction (amount, type, location, accountId) VALUES (?, ?, ?, ?)";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      s.setDouble(1, tx.getBalance());
      s.setString(2, toDb(tx.getType()));
      s.setString(3, tx.getPlace());
      s.setString(4, tx.getAccountId());
      s.executeUpdate();
      try (ResultSet keys = s.getGeneratedKeys()) {
        if (keys.next()) {
          int id = keys.getInt(1);
          tx.setId(id);
          return id;
        }
      }
      return -1;
    } catch (Exception e) {
      throw new DbException("Cannot add transaction: " + e.getMessage(), e);
    }
  }

  public static List<Transaction> findByAccount(String accountId) {
    List<Transaction> list = new ArrayList<Transaction>();
    String sql = "SELECT * FROM Transaction WHERE accountId = ? ORDER BY date";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setString(1, accountId);
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          list.add(rowToTx(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot load history: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Transaction> findAll() {
    List<Transaction> list = new ArrayList<Transaction>();
    String sql = "SELECT * FROM Transaction ORDER BY date";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql);
        ResultSet r = s.executeQuery()) {
      while (r.next()) {
        list.add(rowToTx(r));
      }
    } catch (Exception e) {
      throw new DbException("Cannot list transactions: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Transaction> findByClient(int clientId) {
    List<Transaction> list = new ArrayList<Transaction>();
    String sql = "SELECT t.* FROM Transaction t JOIN Account cp ON cp.id = t.accountId WHERE cp.customerId = ? ORDER BY t.date";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setInt(1, clientId);
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          list.add(rowToTx(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot find client transactions: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Transaction> findByType(TransactionType type) {
    List<Transaction> list = new ArrayList<Transaction>();
    String sql = "SELECT * FROM Transaction WHERE type = ? ORDER BY date";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setString(1, toDb(type));
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          list.add(rowToTx(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot filter by type: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Transaction> findByAmountRange(double min, double max) {
    List<Transaction> list = new ArrayList<Transaction>();
    String sql = "SELECT * FROM Transaction WHERE amount >= ? AND amount <= ? ORDER BY date";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setDouble(1, min);
      s.setDouble(2, max);
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          list.add(rowToTx(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot filter by amount: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Transaction> findByPlace(String place) {
    List<Transaction> list = new ArrayList<Transaction>();
    String sql = "SELECT * FROM Transaction WHERE location = ? ORDER BY date";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setString(1, place);
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          list.add(rowToTx(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot filter by place: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Transaction> findSuspectMontant(double threshold) {
    List<Transaction> list = new ArrayList<Transaction>();
    String sql = "SELECT * FROM Transaction WHERE amount > ? ORDER BY date";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setDouble(1, threshold);
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          list.add(rowToTx(r));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot find suspects: " + e.getMessage(), e);
    }
    return list;
  }

  public static Map<TransactionType, Integer> countByType() {
    Map<TransactionType, Integer> out = new EnumMap<TransactionType, Integer>(TransactionType.class);
    out.put(TransactionType.DEPOSIT, 0);
    out.put(TransactionType.WITHDRAW, 0);
    out.put(TransactionType.TRANSFER, 0);
    String sql = "SELECT type, COUNT(*) FROM Transaction GROUP BY type";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql);
        ResultSet r = s.executeQuery()) {
      while (r.next()) {
        out.put(fromDb(r.getString(1)), r.getInt(2));
      }
    } catch (Exception e) {
      throw new DbException("Cannot count by type: " + e.getMessage(), e);
    }
    return out;
  }

  public static MonthlyStats monthlyStats(int year, int month) {
    String sql = "SELECT type, COUNT(*), COALESCE(SUM(amount),0) FROM Transaction "
        + "WHERE YEAR(date) = ? AND MONTH(date) = ? GROUP BY type";
    int depositCount = 0;
    int withdrawCount = 0;
    int transferCount = 0;
    double volume = 0;
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setInt(1, year);
      s.setInt(2, month);
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          TransactionType t = fromDb(r.getString(1));
          int n = r.getInt(2);
          volume = volume + r.getDouble(3);
          switch (t) {
            case DEPOSIT -> depositCount = n;
            case WITHDRAW -> withdrawCount = n;
            case TRANSFER -> transferCount = n;
          }
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot build monthly report: " + e.getMessage(), e);
    }
    return new MonthlyStats(depositCount, withdrawCount, transferCount, volume);
  }
}
