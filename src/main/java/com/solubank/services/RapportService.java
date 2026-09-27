package com.solubank.services;

import com.solubank.dao.AccountDAO;
import com.solubank.dao.ClientDAO;
import com.solubank.dao.TransactionDAO;
import com.solubank.models.Account;
import com.solubank.dto.ClientTotal;
import com.solubank.dto.MonthlyStats;
import com.solubank.models.Transaction;
import java.time.LocalDate;
import java.util.List;

public class RapportService {

  public static void printTop5() {
    List<ClientTotal> top = ClientDAO.topBySoldeTotal(5);
    System.out.println("--- Top 5 clients by balance ---");
    for (int i = 0; i < top.size(); i++) {
      ClientTotal ct = top.get(i);
      System.out.println((i + 1) + ". " + ct.getClient().getName() + " (" + ct.getClient().getEmail() + ") = " + ct.getTotal());
    }
    if (top.size() == 0) {
      System.out.println("No clients.");
    }
  }

  public static void printMonthlyReport(int year, int month) {
    MonthlyStats s = TransactionDAO.monthlyStats(year, month);
    System.out.println("--- Report " + month + "/" + year + " ---");
    System.out.println("Deposits: " + s.getDeposits());
    System.out.println("Withdrawals: " + s.getWithdrawals());
    System.out.println("Transfers: " + s.getTransfers());
    System.out.println("Total volume: " + s.getVolume());
  }

  public static List<Transaction> suspectAmount() {
    return TransactionDAO.findSuspectMontant(10000);
  }

  public static List<Account> inactiveAccounts(int jours) {
    LocalDate cutoff = LocalDate.now().minusDays(jours);
    return AccountDAO.findInactiveSince(cutoff);
  }

  public static void printAlerts() {
    System.out.println("--- Alerts ---");
    List<Account> low = AccountService.lowBalance(100);
    System.out.println("Low balance (<100): " + low.size());
    for (int i = 0; i < low.size(); i++) {
      System.out.println(" - " + low.get(i).getNumber() + " balance=" + low.get(i).getBalance());
    }
    List<Account> inactive = inactiveAccounts(90);
    System.out.println("Inactive (90 days): " + inactive.size());
    for (int i = 0; i < inactive.size(); i++) {
      System.out.println(" - " + inactive.get(i).getNumber());
    }
    List<Transaction> susp = suspectAmount();
    System.out.println("Suspects (>10000): " + susp.size());
    for (int i = 0; i < susp.size(); i++) {
      System.out.println(" - tx " + susp.get(i).getId() + " amount=" + susp.get(i).getBalance());
    }
  }
}
