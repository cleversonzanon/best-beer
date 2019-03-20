package beer.me.bestbeer.exception;

public class PlaylistNotFoundException extends RuntimeException {

  public PlaylistNotFoundException(final String message) {
    super(message);
  }
}
