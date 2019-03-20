package beer.me.bestbeer.exception;

public class TrackWithoutArtistException extends RuntimeException {

  public TrackWithoutArtistException(final String message) {
    super(message);
  }
}
