package com.solubank.ui;

import java.util.Scanner;

public class Input {
  static Scanner sc = new Scanner(System.in);

  public static String readText(String label) {
    System.out.print(label);
    return sc.nextLine().trim();
  }

  public static int readInt(String label) {
    System.out.print(label);
    String s = sc.nextLine().trim();
    try {
      return Integer.parseInt(s);
    } catch (Exception e) {
      System.out.println("Invalid number, using 0.");
      return 0;
    }
  }

  public static double readDouble(String label) {
    System.out.print(label);
    String s = sc.nextLine().trim();
    try {
      return Double.parseDouble(s);
    } catch (Exception e) {
      System.out.println("Invalid amount, using 0.");
      return 0;
    }
  }
}
