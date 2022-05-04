package org.bodyaq.jsnippet.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class ByteArrayJavaFileObject extends SimpleJavaFileObject {
  private final String className;
  private ByteArrayOutputStream bos = new ByteArrayOutputStream();
  private byte[] bytes = null;

  public ByteArrayJavaFileObject(String name) {
    super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
    this.className = name;
  }

  public void dump() {
    try {
      Path dumpDir = FileSystems.getDefault().getPath("dump");
      if (Files.notExists(dumpDir)) {
        Files.createDirectory(dumpDir);
      }
      Path file = FileSystems.getDefault().getPath("dump", getName() + ".class");
      Files.write(file, getBytes());
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
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