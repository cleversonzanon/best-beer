package beer.me.bestbeer.exception;

/** Exception thrown when there's a track without an artist related */
public class TrackWithoutArtistException extends RuntimeException {

  public TrackWithoutArtistException(final String message) {
    super(message);
  }
}
