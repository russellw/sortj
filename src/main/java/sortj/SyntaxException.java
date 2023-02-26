package sortj;

public final class SyntaxException extends RuntimeException {
  final int index;

  public SyntaxException(int index) {
    this.index = index;
  }
}
