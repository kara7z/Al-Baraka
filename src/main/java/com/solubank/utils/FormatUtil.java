package com.solubank.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtil {

  public static String formatAmount(double amount) {

    return String.format("%.2f", amount);
  }

  public static String formatDate(LocalDateTime date) {
    if (date == null) {
      return "-";
    }
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return date.format(fmt);
  }

  public static String typeLabel(String type) {
    if (type == null) {
      return "?";
    }
    return switch (type) {
      case "VERSEMENT" -> "Versement";
      case "RETRAIT" -> "Retrait";
      case "VIREMENT" -> "Virement";
      default -> type;
    };
  }
}
