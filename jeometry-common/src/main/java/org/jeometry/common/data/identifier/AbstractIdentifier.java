package org.jeometry.common.data.identifier;

import java.util.List;

import org.jeometry.common.data.type.DataType;
import org.jeometry.common.data.type.DataTypes;

public abstract class AbstractIdentifier implements Identifier {
  @Override
  public boolean equals(final Object other) {
    if (other == this) {
      return true;
    } else if (other instanceof Identifier) {
      final List<Object> values = getValues();
      final Identifier recordIdentifier = (Identifier)other;
      final List<Object> otherValues = recordIdentifier.getValues();
      if (DataType.equal(values, otherValues)) {
        return true;
      } else {
        return false;
      }
    } else {
      final List<Object> values = getValues();
      if (values.size() == 1) {
        return values.get(0).equals(other);
      } else {
        return false;
      }
    }
  }

  @Override
  public int hashCode() {
    int hashCode;
    final List<Object> values = getValues();
    if (values.size() == 1) {
      hashCode = values.get(0).hashCode();
    } else {
      hashCode = 1;
      for (final Object value : values) {
        hashCode *= 31;
        if (value != null) {
          hashCode += value.hashCode();
        }
      }
    }
    return hashCode;
  }

  @Override
  public String toString() {
    final List<Object> values = getValues();
    if (values == null) {
      return null;
    } else {
      final StringBuilder result = new StringBuilder();
      boolean first = true;
      for (final Object value : values) {
        if (value != null) {
          final String string = DataTypes.toString(value);
          if (string != null) {
            if (first) {
              first = false;
            } else {
              result.append(":");
            }
            result.append(string);
          }
        }
      }
      return result.toString();
    }
  }
}
