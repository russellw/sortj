package sortj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

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

    var out = new ArrayList<>(in);
    for (var i = out.size(); i-- > 0; ) if (out.get(i).strip().equals("// SORT")) new Sort(out, i);

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
    for (var s : out) {
      System.out.print(s);
      System.out.print('\n');
    }
  }

  public static void main(String[] args) throws IOException {
    Option.parse(OPTIONS, args);
    if (Option.positionalArgs.isEmpty()) {
      System.err.print("Usage: sortj [options] files\n");
      System.exit(1);
    }
    if (Option.positionalArgs.size() > 1 && !inPlace) {
      System.err.print("Multiple files specified without -i\n");
      System.exit(1);
    }
    for (var file : Option.positionalArgs) {
      if (!Etc.ext(file).equals("java")) {
        System.err.print(file + ": not a Java source file - skipped\n");
        continue;
      }
      sort(file);
    }
  }
}
