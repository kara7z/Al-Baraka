package com.solubank.dto;

import com.solubank.models.Client;

public class ClientTotal {
  private Client client;
  private double total;

  public ClientTotal(Client client, double total) {
    this.client = client;
    this.total = total;
  }

  public Client getClient() {
    return client;
  }

  public double getTotal() {
    return total;
  }
}
