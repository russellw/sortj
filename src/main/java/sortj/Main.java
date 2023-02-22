package sortj;

import java.io.IOException;

public final class Main {
  private static boolean inPlace;
  private static final Option[] OPTIONS =
      new Option[] {
        new Option("sort files in place", null, "i") {
          void accept(String arg) {
            inPlace = true;
          }
        },
      };

  private static void sort(String file) {
    System.out.println(file);
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
