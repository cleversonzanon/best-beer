package beer.me.bestbeer.exception;

/** Exception thrown when there's an error while fetching the spotify playlist from it's API */
public class FetchSpotifyPlaylistException extends RuntimeException {

  public FetchSpotifyPlaylistException(final Exception exception) {
    super(exception);
  }
}
