package com.yerlibilgin.biometrics;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author yerlibilgin
 */
public class ISO2XYT {

  /**
   * Main method
   */
  public static void main(String[] args) {

    if (args.length != 2) {
      printUsage();
      System.exit(-1);
      return;
    }

    File isoFile = new File(args[0]);
    if (!isoFile.exists()) {
      System.err.println("No such file \"" + args[0] + "\"");
      System.exit(-2);
      return;
    }

    File xytFile = new File(args[1]);

    try (FileOutputStream fos = new FileOutputStream(xytFile)) {
      final byte[] bytes = Files.readAllBytes(isoFile.toPath());
      byte[] xyt = iso2xyt(bytes);
      fos.write(xyt);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(-3);
      return;
    }

    System.out.println("Done...");
  }

  private static void printUsage() {
    System.err.println("Usage");
    System.err.println("   iso2xyt infile outfile");
  }

  /**
   * Converts the given ISO 19794-2 FMR data to XYT data
   *
   * @return the XYT values derived from the FMR
   * @throws IllegalStateException
   *     If there is a problem during the conversion
   */
  public static byte[] iso2xyt(byte[] iso) {
    try {
      if (iso.length < 27) {
        throw new IOException("insufficient iso buffer");
      }
      DataInputStream dis = new DataInputStream(new ByteArrayInputStream(iso));
      if (dis.skip(27) < 27) {
        throw new IOException("insufficient iso buffer");
      }

      int numMinutia = dis.read() & 0x0FF; // unsigned byte make it unsigned byte
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < numMinutia; ++i) {
        //@formatter:off
      builder.append(0x3FFF & dis.readShort()).append('\t')   //X
          .append(0x3FFF & dis.readShort()).append('\t')   //Y
          .append((int) ((dis.read() & 0x0FF) * 90.0 / 128))  //Theta
          .append('\t').append(dis.read() & 0x0FF)         //Quality;
          .append('\n');
      //@formatter:on
      }
      return builder.toString().getBytes();
    } catch (Throwable th) {
      throw new ISO2XYTException(th.getMessage(), th);
    }
  }
}
