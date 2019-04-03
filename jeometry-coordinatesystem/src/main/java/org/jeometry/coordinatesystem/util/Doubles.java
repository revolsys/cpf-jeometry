package org.jeometry.coordinatesystem.util;

public class Doubles {
  private static final double[] POWERS_OF_TEN_DOUBLE = new double[30];

  private static final long[] POWERS_OF_TEN_LONG = new long[19];

  static {
    POWERS_OF_TEN_LONG[0] = 1L;
    for (int i = 1; i < POWERS_OF_TEN_LONG.length; i++) {
      POWERS_OF_TEN_LONG[i] = POWERS_OF_TEN_LONG[i - 1] * 10L;
    }
    for (int i = 0; i < POWERS_OF_TEN_DOUBLE.length; i++) {
      POWERS_OF_TEN_DOUBLE[i] = Double.parseDouble("1e" + i);
    }
  }

  private static final double MIN_VALUE = 4.999999999999999 / 1e20;

  private static void format(final StringBuilder target, long intP, long decP) {
    int scale = 19;
    if (decP != 0L) {
      // decP is the decimal part of source, truncated to scale + 1 digit.
      // Custom rounding: add 5
      decP += 5L;
      decP /= 10L;
      if (decP >= tenPowDouble(scale)) {
        intP++;
        decP -= tenPow(scale);
      }
      if (decP != 0L) {
        // Remove trailing zeroes
        while (decP % 10L == 0L) {
          decP = decP / 10L;
          scale--;
        }
      }
    }
    target.append(intP);
    if (decP != 0L) {
      target.append('.');
      // Use tenPow instead of tenPowDouble for scale below 18,
      // since the casting of decP to double may cause some imprecisions:
      // E.g. for decP = 9999999999999999L and scale = 17,
      // decP < tenPow(16) while (double) decP == tenPowDouble(16)
      while (scale > 0 && (scale > 18 ? decP < tenPowDouble(--scale) : decP < tenPow(--scale))) {
        // Insert leading zeroes
        target.append('0');
      }
      target.append(decP);
    }
  }

  private static long tenPow(final int n) {
    return n < POWERS_OF_TEN_LONG.length ? POWERS_OF_TEN_LONG[n] : (long)Math.pow(10, n);
  }

  private static double tenPowDouble(final int n) {
    return n < POWERS_OF_TEN_DOUBLE.length ? POWERS_OF_TEN_DOUBLE[n] : Math.pow(10, n);
  }

  public static String toString(double source) {
    if (Double.isNaN(source)) {
      return "NaN";
    } else if (source == 0.0 || Math.abs(source) < MIN_VALUE) {
      return "0";
    } else if (!Double.isFinite(source)) {
      return Double.toString(source);
    } else {
      final StringBuilder target = new StringBuilder();

      final boolean negative = source < 0.0;
      if (negative) {
        source = -source;
        target.append('-');
      }
      // The only way to format precisely the double is to use the String
      // representation of the double, and then to do mathematical integer
      // operation on it.
      final String s = Double.toString(source);
      if (source >= 1e-3 && source < 1e7) {
        // Plain representation of double: "intPart.decimalPart"
        final int dot = s.indexOf('.');
        String decS = s.substring(dot + 1);
        int decLength = decS.length();
        if (19 >= decLength) {
          if ("0".equals(decS)) {
            // source is a mathematical integer
            target.append(s.substring(0, dot));
          } else {
            target.append(s);
            // Remove trailing zeroes
            for (int l = target.length() - 1; l >= 0 && target.charAt(l) == '0'; l--) {
              target.setLength(l);
            }
          }
          return target.toString();
        } else if (20 < decLength) {
          // ignore unnecessary digits
          decLength = 20;
          decS = decS.substring(0, decLength);
        }
        final long intP = Long.parseLong(s.substring(0, dot));
        final long decP = Long.parseLong(decS);
        format(target, intP, decP);
      } else {
        // Scientific representation of double: "x.xxxxxEyyy"
        final int dot = s.indexOf('.');
        final int exp = s.indexOf('E');
        int exposant = Integer.parseInt(s.substring(exp + 1));
        final String intS = s.substring(0, dot);
        final String decS = s.substring(dot + 1, exp);
        final int decLength = decS.length();
        if (exposant >= 0) {
          final int digits = decLength - exposant;
          if (digits <= 0) {
            // no decimal part,
            // no rounding involved
            target.append(intS);
            target.append(decS);
            for (int i = -digits; i > 0; i--) {
              target.append('0');
            }
          } else if (digits <= 19) {
            // decimal part precision is lower than scale,
            // no rounding involved
            target.append(intS);
            target.append(decS.substring(0, exposant));
            target.append('.');
            target.append(decS.substring(exposant));
          } else {
            // decimalDigits > scale,
            // Rounding involved
            final long intP = Long.parseLong(intS) * tenPow(exposant)
              + Long.parseLong(decS.substring(0, exposant));
            final long decP = Long.parseLong(decS.substring(exposant, exposant + 20));
            format(target, intP, decP);
          }
        } else {
          // Only a decimal part is supplied
          exposant = -exposant;
          final int digits = 19 - exposant + 1;
          if (digits < 0) {
            target.append('0');
          } else if (digits == 0) {
            final long decP = Long.parseLong(intS);
            format(target, 0L, decP);
          } else if (decLength < digits) {
            final long decP = Long.parseLong(intS) * tenPow(decLength + 1)
              + Long.parseLong(decS) * 10;
            format(target, 0L, decP);
          } else {
            final long subDecP = Long.parseLong(decS.substring(0, digits));
            final long decP = Long.parseLong(intS) * tenPow(digits) + subDecP;
            format(target, 0L, decP);
          }
        }
      }
      return target.toString();
    }
  }

}
