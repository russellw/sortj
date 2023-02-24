package sortj;

public final class TabException extends RuntimeException {
  final int index;

  public TabException(int index) {
    this.index = index;
  }
}
