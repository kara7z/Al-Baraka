package com.solubank.utils;

import java.security.SecureRandom;

public class AccountNumberGenerator {
  private static final SecureRandom random = new SecureRandom();

  public static String generate() {
    StringBuilder number = new StringBuilder(24);
    for (int i = 0; i < 24; i++) {
      number.append(random.nextInt(10));
    }
    return number.toString();
  }

  static String generateNumber() {
    return generate();
  }
}
