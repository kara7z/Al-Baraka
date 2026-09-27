package com.solubank.dao;

import com.solubank.models.Client;
import com.solubank.dto.ClientTotal;
import com.solubank.utils.DBConnection;
import com.solubank.utils.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

  public static int create(Client client) {
    String sql = "INSERT INTO Customer (name, email) VALUES (?, ?)";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      s.setString(1, client.getName());
      s.setString(2, client.getEmail());
      s.executeUpdate();
      try (ResultSet keys = s.getGeneratedKeys()) {
        if (keys.next()) {
          int id = keys.getInt(1);
          client.setId(id);
          return id;
        }
      }
      return -1;
    } catch (Exception e) {
      throw new DbException("Cannot add client: " + e.getMessage(), e);
    }
  }

  public static Client findById(int id) {
    String sql = "SELECT id, name, email FROM Customer WHERE id = ?";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setInt(1, id);
      try (ResultSet r = s.executeQuery()) {
        if (r.next()) {
          Client client = new Client(r.getString("name"), r.getString("email"));
          client.setId(r.getInt("id"));
          return client;
        }
        return null;
      }
    } catch (Exception e) {
      throw new DbException("Cannot find client: " + e.getMessage(), e);
    }
  }

  public static List<Client> findByName(String name) {
    List<Client> list = new ArrayList<Client>();
    String sql = "SELECT id, name, email FROM Customer WHERE name LIKE ?";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setString(1, "%" + name + "%");
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          Client client = new Client(r.getString("name"), r.getString("email"));
          client.setId(r.getInt("id"));
          list.add(client);
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot search by name: " + e.getMessage(), e);
    }
    return list;
  }

  public static List<Client> findAll() {
    List<Client> list = new ArrayList<Client>();
    String sql = "SELECT id, name, email FROM Customer ORDER BY id";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql);
        ResultSet r = s.executeQuery()) {
      while (r.next()) {
        Client client = new Client(r.getString("name"), r.getString("email"));
        client.setId(r.getInt("id"));
        list.add(client);
      }
    } catch (Exception e) {
      throw new DbException("Cannot list clients: " + e.getMessage(), e);
    }
    return list;
  }

  public static boolean update(Client client) {
    String sql = "UPDATE Customer SET name = ?, email = ? WHERE id = ?";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setString(1, client.getName());
      s.setString(2, client.getEmail());
      s.setInt(3, client.getId());
      return s.executeUpdate() > 0;
    } catch (Exception e) {
      throw new DbException("Cannot update client: " + e.getMessage(), e);
    }
  }

  public static boolean delete(int id) {
    String sql = "DELETE FROM Customer WHERE id = ?";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setInt(1, id);
      return s.executeUpdate() > 0;
    } catch (Exception e) {
      throw new DbException("Cannot delete client: " + e.getMessage(), e);
    }
  }

  public static List<ClientTotal> topBySoldeTotal(int n) {
    List<ClientTotal> list = new ArrayList<ClientTotal>();
    String sql = "SELECT c.id, c.name, c.email, COALESCE(SUM(cp.balance),0) AS total "
        + "FROM Customer c LEFT JOIN Account cp ON cp.customerId = c.id "
        + "GROUP BY c.id, c.name, c.email ORDER BY total DESC LIMIT ?";
    try (Connection c = DBConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
      s.setInt(1, n);
      try (ResultSet r = s.executeQuery()) {
        while (r.next()) {
          Client client = new Client(r.getString("name"), r.getString("email"));
          client.setId(r.getInt("id"));
          list.add(new ClientTotal(client, r.getDouble("total")));
        }
      }
    } catch (Exception e) {
      throw new DbException("Cannot load top clients: " + e.getMessage(), e);
    }
    return list;
  }
}
