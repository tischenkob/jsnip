package org.bodyaq.jsnip.cli;

import org.bodyaq.jsnip.Jsnip;

public class JsnipCli {
  public static void main(String[] args) {
    var jsnip = new Jsnip();
    System.out.println(jsnip.getApplicationName());
  }
}
