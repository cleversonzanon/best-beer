package beer.me.bestbeer.exception;

/** Exception thrown when there's an error while retrieving the spotify credentials */
public class SpotifyCredentialsException extends RuntimeException {

  public SpotifyCredentialsException(final Exception exception) {
    super(exception);
  }
}
