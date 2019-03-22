package beer.me.bestbeer.exception;

/** Exception thrown when a Spotify playlist is not found for a given beer style */
public class PlaylistNotFoundException extends RuntimeException {

  public PlaylistNotFoundException() {
    super();
  }

  public PlaylistNotFoundException(final String message) {
    super(message);
  }
}
