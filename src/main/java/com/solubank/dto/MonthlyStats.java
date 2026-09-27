package com.solubank.dto;

public class MonthlyStats {
  private int deposits;
  private int withdrawals;
  private int transfers;
  private double volume;

  public MonthlyStats(int deposits, int withdrawals, int transfers, double volume) {
    this.deposits = deposits;
    this.withdrawals = withdrawals;
    this.transfers = transfers;
    this.volume = volume;
  }

  public int getDeposits() {
    return deposits;
  }

  public int getWithdrawals() {
    return withdrawals;
  }

  public int getTransfers() {
    return transfers;
  }

  public double getVolume() {
    return volume;
  }
}
