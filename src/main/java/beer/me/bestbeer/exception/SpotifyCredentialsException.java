package beer.me.bestbeer.exception;

public class SpotifyCredentialsException extends RuntimeException {

  public SpotifyCredentialsException(final Exception exception) {
    super(exception);
  }
}
