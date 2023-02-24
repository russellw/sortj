package sortj;

public final class IndentException extends RuntimeException {
  final int index;

  public IndentException(int index) {
    this.index = index;
  }
}
