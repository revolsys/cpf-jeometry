package org.jeometry.common.datatype;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class CollectionDataType extends SimpleDataType {

  private final DataType contentType;

  public CollectionDataType(final String name, final Class<?> javaClass,
    final DataType contentType) {
    super(name, javaClass);
    this.contentType = contentType;
  }

  public DataType getContentType() {
    return this.contentType;
  }

  @Override
  protected Object toObjectDo(final Object value) {
    if (value instanceof Collection) {
      try {
        final Collection<?> collection = (Collection<?>)value;
        final Class<?> javaClass = getJavaClass();
        final Constructor<?> declaredConstructor = javaClass.getDeclaredConstructor();
        @SuppressWarnings({
          "unchecked", "rawtypes"
        })
        final Collection<Object> newCollection = (Collection)declaredConstructor.newInstance();
        newCollection.addAll(collection);
        return newCollection;
      } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
          | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    } else {
      return super.toObjectDo(value);
    }
  }
}
