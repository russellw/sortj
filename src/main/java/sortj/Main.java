package sortj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class Main {
  private static boolean inPlace;
  private static final Option[] OPTIONS =
      new Option[] {
        new Option("sort files in place", null, "i", "r", "replace") {
          void accept(String arg) {
            inPlace = true;
          }
        },
      };

  private static void sort(String file) throws IOException {
    var path = Path.of(file);
    var in = Files.readAllLines(path, StandardCharsets.UTF_8);
    var out = new Sort(in).out;
    if (inPlace) {
      Files.move(
          path,
          Path.of(System.getProperty("java.io.tmpdir"), new File(file).getName()),
          StandardCopyOption.REPLACE_EXISTING);
      try (var writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
        for (var s : out) {
          writer.write(s);
          writer.write('\n');
        }
      }
      return;
    }
    for (var s : out) System.out.println(s);
  }

  public static void main(String[] args) throws IOException {
    Option.parse(OPTIONS, args);
    if (Option.positionalArgs.isEmpty()) {
      System.err.println("Usage: sortj [options] files");
      System.exit(1);
    }
    if (Option.positionalArgs.size() > 1 && !inPlace) {
      System.err.println("Multiple files specified without -i");
      System.exit(1);
    }
    for (var file : Option.positionalArgs) {
      if (!Etc.ext(file).equals("java")) {
        System.err.println(file + ": not a Java source file - skipped");
        continue;
      }
      sort(file);
    }
  }
}
