package beer.me.bestbeer.exception;

public class FetchSpotifyPlaylistException extends RuntimeException {

  public FetchSpotifyPlaylistException(final Exception exception) {
    super(exception);
  }
}
