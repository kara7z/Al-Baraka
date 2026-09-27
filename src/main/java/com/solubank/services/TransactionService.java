package com.solubank.services;

import com.solubank.dao.AccountDAO;
import com.solubank.dao.TransactionDAO;
import com.solubank.enums.TransactionType;
import com.solubank.models.Account;
import com.solubank.models.CurrentAccount;
import com.solubank.models.Transaction;
import com.solubank.utils.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionService {

  public static boolean deposit(String number, double amount, String place) {
    if (!Validator.isValidAmount(amount)) {
      System.out.println("Invalid amount (must be > 0).");
      return false;
    }
    Account c = AccountDAO.findByNumber(number);
    if (c == null) {
      System.out.println("Account not found.");
      return false;
    }
    double nouveau = c.getBalance() + amount;
    AccountDAO.updateBalance(c.getId(), nouveau);
    Transaction tx = new Transaction(amount, TransactionType.DEPOSIT, place, c.getId());
    TransactionDAO.create(tx);
    System.out.println("Deposit OK. New balance = " + nouveau);
    return true;
  }

  public static boolean withdraw(String number, double amount, String place) {
    if (!Validator.isValidAmount(amount)) {
      System.out.println("Invalid amount (must be > 0).");
      return false;
    }
    Account c = AccountDAO.findByNumber(number);
    if (c == null) {
      System.out.println("Account not found.");
      return false;
    }
    double cutoff = 0;
    if (c instanceof CurrentAccount) {
      CurrentAccount cc = (CurrentAccount) c;
      cutoff = -cc.getOverdraftLimit();
    }
    if (c.getBalance() - amount < cutoff) {
      System.out.println("Insufficient balance (overdraft exceeded).");
      return false;
    }
    double nouveau = c.getBalance() - amount;
    AccountDAO.updateBalance(c.getId(), nouveau);
    Transaction tx = new Transaction(amount, TransactionType.WITHDRAW, place, c.getId());
    TransactionDAO.create(tx);
    System.out.println("Withdraw OK. New balance = " + nouveau);
    return true;
  }

  public static boolean transfer(String numFrom, String numTo, double amount, String place) {
    if (numFrom.equals(numTo)) {
      System.out.println("Source and destination accounts are identical.");
      return false;
    }
    Account src = AccountDAO.findByNumber(numFrom);
    Account dst = AccountDAO.findByNumber(numTo);
    if (src == null || dst == null) {
      System.out.println("Source or destination account not found.");
      return false;
    }
    boolean ok = withdraw(numFrom, amount, place);
    if (!ok) {
      return false;
    }
    deposit(numTo, amount, place);
    Transaction tx = new Transaction(amount, TransactionType.TRANSFER, place, dst.getId());
    TransactionDAO.create(tx);
    System.out.println("Transfer OK.");
    return true;
  }

  public static List<Transaction> historyByAccount(String number) {
    Account c = AccountDAO.findByNumber(number);
    if (c == null) {
      return new ArrayList<Transaction>();
    }
    return TransactionDAO.findByAccount(c.getId());
  }

  public static List<Transaction> historyByClient(int clientId) {
    return TransactionDAO.findByClient(clientId);
  }

  public static List<Transaction> filterByAmount(double min, double max) {
    return TransactionDAO.findByAmountRange(min, max);
  }

  public static List<Transaction> filterByType(TransactionType type) {
    return TransactionDAO.findByType(type);
  }

  public static List<Transaction> filterByPlace(String place) {
    return TransactionDAO.findByPlace(place);
  }

  public static double totalByAccount(String number) {
    List<Transaction> list = historyByAccount(number);
    double total = 0;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getType() == TransactionType.DEPOSIT) {
        total = total + list.get(i).getBalance();
      } else if (list.get(i).getType() == TransactionType.WITHDRAW) {
        total = total - list.get(i).getBalance();
      }
    }
    return total;
  }

  public static double averageByAccount(String number) {
    List<Transaction> list = historyByAccount(number);
    if (list.size() == 0) {
      return 0;
    }
    double sum = 0;
    for (int i = 0; i < list.size(); i++) {
      sum = sum + list.get(i).getBalance();
    }
    return sum / list.size();
  }

  public static void printGroupByType() {
    Map<TransactionType, Integer> counts = TransactionDAO.countByType();
    System.out.println("DEPOSIT: " + counts.get(TransactionType.DEPOSIT));
    System.out.println("WITHDRAW: " + counts.get(TransactionType.WITHDRAW));
    System.out.println("TRANSFER: " + counts.get(TransactionType.TRANSFER));
  }
}
