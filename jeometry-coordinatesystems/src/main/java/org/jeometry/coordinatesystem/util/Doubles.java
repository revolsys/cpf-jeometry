package org.jeometry.coordinatesystem.util;

public interface Doubles {

  static String toString( final double number) {
    if (Double.isNaN(number)) {
      return "NaN";
    } else {
      StringBuilder string = new StringBuilder();
      DoubleFormatUtil.formatDoublePrecise(number, 19, 19, string);
      return string.toString();
    }
  }
}
