package com.solubank.services;

import com.solubank.dao.AccountDAO;
import com.solubank.dao.ClientDAO;
import com.solubank.models.Client;
import com.solubank.utils.Validator;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

  public static Client addClient(String name, String email) {
    if (!Validator.isValidName(name)) {
      System.out.println("Invalid name (2 letters minimum).");
      return null;
    }
    if (!Validator.isValidEmail(email)) {
      System.out.println("Invalid email.");
      return null;
    }
    Client c = new Client(name.trim(), email.trim());
    try {
      ClientDAO.create(c);
      System.out.println("Client added, id = " + c.getId());
      return c;
    } catch (Exception e) {
      System.out.println("Error adding client: " + e.getMessage());
      return null;
    }
  }

  public static Client searchById(int id) {
    return ClientDAO.findById(id);
  }

  public static List<Client> searchByName(String name) {
    if (name == null || name.trim().length() == 0) {
      return new ArrayList<Client>();
    }
    return ClientDAO.findByName(name.trim());
  }

  public static List<Client> listAll() {
    return ClientDAO.findAll();
  }

  public static double balanceTotal(int clientId) {
    return AccountDAO.balanceTotalByClient(clientId);
  }

  public static int accountCount(int clientId) {
    return AccountDAO.countByClient(clientId);
  }
}
