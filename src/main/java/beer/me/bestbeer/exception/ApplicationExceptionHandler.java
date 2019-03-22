package beer.me.bestbeer.exception;

import beer.me.bestbeer.util.RestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** The Application's exception handler. */
@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

  /* MESSAGE KEYS */

  /* TREATED EXCEPTIONS, 400 */
  private static final String BEER_NOT_FOUND = "Exception.beer.notFound";
  private static final String PLAYLIST_NOT_FOUND = "Exception.playlist.notFound";

  /* SPOTIFY API EXCEPTIONS */
  private static final String FETCH_SPOTIFY_PLAYLIST = "Exception.spotify.fetchPlaylist";
  private static final String SPOTIFY_CREDENTIALS = "Exception.spotify.credentials";
  private static final String TRACK_WITHOUT_ARTIST = "Exception.track.withoutArtist";

  /* INTERNAL SERVER ERROR, UNEXPECTED, 500 */
  private static final String UNEXPECTED_ERROR = "Exception.unexpected";

  private final MessageSource messageSource;

  /* EXCEPTIONS MAPPING */

  /* BEAN VALIDATION EXCEPTIONS */

  /**
   * Handles MethodArgumentNotValidException exception
   *
   * @param exception the exception
   * @param locale the locale
   * @return the response entity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestMessage> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception, Locale locale) {
    // Get the exception's binding result
    final BindingResult result = exception.getBindingResult();
    // The the list of errors
    final List<String> errorMessageList =
        result.getAllErrors().stream()
            .map(
                objectError ->
                    messageSource.getMessage(objectError, locale)) // Get the message for each error
            .collect(Collectors.toList());
    // Log the exception
    log.error(errorMessageList, exception);
    // Return a response with the list of errors and status 400
    return new ResponseEntity<>(new RestMessage(errorMessageList), HttpStatus.BAD_REQUEST);
  }

  /* TREATED EXCEPTIONS, 400 */

  /**
   * Handles BeerNotFoundException exception
   *
   * @param exception the exception
   * @param locale the locale
   * @return the response entity
   */
  @ExceptionHandler(BeerNotFoundException.class)
  public ResponseEntity<RestMessage> handleBeerNotFoundException(
      BeerNotFoundException exception, Locale locale) {
    // Get the message for this specific exception
    final String errorMessage =
        this.messageSource.getMessage(
            BEER_NOT_FOUND, new Object[] {exception.getMessage()}, locale);
    // Log the exception
    log.error(errorMessage, exception);
    // Return a response with error message and status 400
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles PlaylistNotFoundException exception
   *
   * @param exception the exception
   * @param locale the locale
   * @return the response entity
   */
  @ExceptionHandler(PlaylistNotFoundException.class)
  public ResponseEntity<RestMessage> handlePlaylistNotFoundException(
      PlaylistNotFoundException exception, Locale locale) {
    // Get the message for this specific exception
    final String errorMessage =
        this.messageSource.getMessage(
            PLAYLIST_NOT_FOUND, new Object[] {exception.getMessage()}, locale);
    // Log the exception
    log.error(errorMessage, exception);
    // Return a response with error message and status 400
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.BAD_REQUEST);
  }

  /* SPOTIFY API EXCEPTIONS */

  /**
   * Handles FetchSpotifyPlaylistException exception
   *
   * @param exception the exception
   * @param locale the locale
   * @return the response entity
   */
  @ExceptionHandler(FetchSpotifyPlaylistException.class)
  public ResponseEntity<RestMessage> handleFetchSpotifyPlaylistException(
      FetchSpotifyPlaylistException exception, Locale locale) {
    // Get the message for this specific exception
    final String errorMessage = this.messageSource.getMessage(FETCH_SPOTIFY_PLAYLIST, null, locale);
    // Log the exception
    log.error(errorMessage, exception);
    // Return a response with error message and status 500
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles SpotifyCredentialsException exception
   *
   * @param exception the exception
   * @param locale the locale
   * @return the response entity
   */
  @ExceptionHandler(SpotifyCredentialsException.class)
  public ResponseEntity<RestMessage> handleSpotifyCredentialsException(
      SpotifyCredentialsException exception, Locale locale) {
    // Get the message for this specific exception
    final String errorMessage = this.messageSource.getMessage(SPOTIFY_CREDENTIALS, null, locale);
    // Log the exception
    log.error(errorMessage, exception);
    // Return a response with error message and status 500
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles TrackWithoutArtistException exception
   *
   * @param exception the exception
   * @param locale the locale
   * @return the response entity
   */
  @ExceptionHandler(TrackWithoutArtistException.class)
  public ResponseEntity<RestMessage> handleTrackWithoutArtistException(
      TrackWithoutArtistException exception, Locale locale) {
    // Get the message for this specific exception
    final String errorMessage =
        this.messageSource.getMessage(
            TRACK_WITHOUT_ARTIST, new Object[] {exception.getMessage()}, locale);
    // Log the exception
    log.error(errorMessage, exception);
    // Return a response with error message and status 500
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /* INTERNAL SERVER ERROR, UNEXPECTED, 500 */

  /**
   * Handles any not-mapped exception
   *
   * @param exception the exception
   * @param locale the locale
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestMessage> handleException(Exception exception, Locale locale) {
    // Get the generic message for exceptions
    final String errorMessage = this.messageSource.getMessage(UNEXPECTED_ERROR, null, locale);
    // Log the exception
    log.error(errorMessage, exception);
    // Return a response with error message and status 500
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
