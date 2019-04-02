package org.jeometry.coordinatesystem.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

  public static MessageDigest getMessageDigest() {
    try {
      return MessageDigest.getInstance("MD5");
    } catch (final NoSuchAlgorithmException e) {
      throw new RuntimeException("MD5 Digest not found", e);
    }
  }

  public static void update(final MessageDigest digest, final double value) {
    final long l = Double.doubleToLongBits(value);
    final String data = Long.toString(l);
    update(digest, data);
  }

  public static void update(final MessageDigest digest, final String data) {
    final byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
    digest.update(bytes);
  }
}
