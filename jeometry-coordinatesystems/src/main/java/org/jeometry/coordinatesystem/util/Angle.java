package org.jeometry.coordinatesystem.util;

public class Angle {

  public static final double PI_OVER_2 = Math.PI / 2.0;

  public static final double PI_OVER_4 = Math.PI / 4.0;

  public static final double PI_TIMES_2 = 2.0 * Math.PI;

  public static double asinh(double a) {
    boolean negative = false;
    if (a < 0) {
      negative = true;
      a = -a;
    }

    final double absAsinh = Math.log(a + Math.sqrt(Math.pow(a, 2) + 1));
    return negative ? -absAsinh : absAsinh;
  }

  public static double atanh(double a) {
    boolean negative = false;
    if (a < 0) {
      negative = true;
      a = -a;
    }

    final double absAtanh = 0.5 * Math.log((1 + a) / (1 - a));

    return negative ? -absAtanh : absAtanh;
  }
}
