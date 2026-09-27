package com.solubank.utils;

public class Validator {

  public static boolean isValidName(String name) {
    if (name == null) {
      return false;
    }
    String t = name.trim();
    if (t.length() < 2) {
      return false;
    }
    return true;
  }

  public static boolean isValidEmail(String email) {
    if (email == null) {
      return false;
    }
    String t = email.trim();
    if (t.length() < 5) {
      return false;
    }
    int at = t.indexOf('@');
    int dot = t.lastIndexOf('.');
    if (at < 1) {
      return false;
    }
    if (dot < at + 2) {
      return false;
    }
    if (dot == t.length() - 1) {
      return false;
    }
    return true;
  }

  public static boolean isValidAmount(double amount) {
    if (amount <= 0) {
      return false;
    }
    return true;
  }

  public static boolean isValidPlace(String place) {
    if (place == null) {
      return false;
    }
    if (place.trim().length() == 0) {
      return false;
    }
    return true;
  }

  public static boolean isValidNumber(String number) {
    if (number == null) {
      return false;
    }
    if (number.trim().length() < 4) {
      return false;
    }
    return true;
  }
}
