package org.bodyaq.jsnippet.compile;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class ByteArrayJavaFileObject extends SimpleJavaFileObject {
  private final String className;
  private ByteArrayOutputStream bos = new ByteArrayOutputStream();
  private byte[] bytes = null;

  public ByteArrayJavaFileObject(String name) {
    super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
    this.className = name;
  }

  @Override
  public String getName() {
    return className;
  }

  public byte[] getBytes() {
    if (bytes == null) {
      bytes = bos.toByteArray();
      bos = null;
    }
    return bytes;
  }

  @Override
  public InputStream openInputStream() {
    return new ByteArrayInputStream(getBytes());
  }

  @Override
  public OutputStream openOutputStream() {
    return bos;
  }
}
