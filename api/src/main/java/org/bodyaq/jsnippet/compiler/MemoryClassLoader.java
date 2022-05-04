package org.bodyaq.jsnippet.compiler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class MemoryClassLoader extends ClassLoader {

  private final Map<String, byte[]> classes;

  public MemoryClassLoader(Collection<ByteArrayJavaFileObject> classes) {
    this.classes = groupBytesByName(classes);
  }

  private Map<String, byte[]> groupBytesByName(Collection<ByteArrayJavaFileObject> classes) {
    Map<String, byte[]> map = new HashMap<>();
    for (ByteArrayJavaFileObject object : classes) {
      if (map.put(object.getName(), object.getBytes()) != null) {
        throw new IllegalStateException("Duplicate key");
      }
    }
    return Collections.unmodifiableMap(map);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    assert name != null;

    byte[] data = this.classes.get(name);
    if (data == null) {
      data = this.classes.get(name.replace('.', '/') + ".class");
      if (data == null) {
        throw new ClassNotFoundException(name);
      }
    }

    return super.defineClass(name, data, 0, data.length, this.getClass().getProtectionDomain());
  }
}
