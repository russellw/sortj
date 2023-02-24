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
    // read the input text
    var path = Path.of(file);
    var in = Files.readAllLines(path, StandardCharsets.UTF_8);

    // calculate the sorted text
    var out = new ArrayList<>(in);
    for (var i = out.size(); i-- > 0; ) if (out.get(i).strip().equals("// SORT")) new Sort(out, i);

    // write the sorted text
    if (inPlace) {
      // don't write if nothing has changed
      if (in.equals(out)) return;

      // keep the original for safety
      Files.move(
          path,
          Path.of(System.getProperty("java.io.tmpdir"), new File(file).getName()),
          StandardCopyOption.REPLACE_EXISTING);

      // write the sorted text
      try (var writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
        for (var s : out) {
          writer.write(s);
          writer.write('\n');
        }
      }
      return;
    }

    // avoid println to make sure line endings are consistently LF
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
