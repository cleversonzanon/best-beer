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

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

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

  /* BEAN VALIDATION EXCEPTIONS */

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestMessage> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception, Locale locale) {
    final BindingResult result = exception.getBindingResult();
    final List<String> errorMessageList =
        result.getAllErrors().stream()
            .map(objectError -> messageSource.getMessage(objectError, locale))
            .collect(Collectors.toList());
    log.error(errorMessageList, exception);
    return new ResponseEntity<>(new RestMessage(errorMessageList), HttpStatus.BAD_REQUEST);
  }

  /* TREATED EXCEPTIONS, 400 */

  @ExceptionHandler(BeerNotFoundException.class)
  public ResponseEntity<RestMessage> handleBeerNotFoundException(
      BeerNotFoundException exception, Locale locale) {
    final String errorMessage =
        this.messageSource.getMessage(
            BEER_NOT_FOUND, new Object[] {exception.getMessage()}, locale);
    log.error(errorMessage, exception);
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PlaylistNotFoundException.class)
  public ResponseEntity<RestMessage> handlePlaylistNotFoundException(
      PlaylistNotFoundException exception, Locale locale) {
    final String errorMessage =
        this.messageSource.getMessage(
            PLAYLIST_NOT_FOUND, new Object[] {exception.getMessage()}, locale);
    log.error(errorMessage, exception);
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.BAD_REQUEST);
  }

  /* SPOTIFY API EXCEPTIONS */

  @ExceptionHandler(FetchSpotifyPlaylistException.class)
  public ResponseEntity<RestMessage> handleFetchSpotifyPlaylistException(
      FetchSpotifyPlaylistException exception, Locale locale) {
    final String errorMessage = this.messageSource.getMessage(FETCH_SPOTIFY_PLAYLIST, null, locale);
    log.error(errorMessage, exception);
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(SpotifyCredentialsException.class)
  public ResponseEntity<RestMessage> handleSpotifyCredentialsException(
      SpotifyCredentialsException exception, Locale locale) {
    final String errorMessage = this.messageSource.getMessage(SPOTIFY_CREDENTIALS, null, locale);
    log.error(errorMessage, exception);
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(TrackWithoutArtistException.class)
  public ResponseEntity<RestMessage> handleTrackWithoutArtistException(
      TrackWithoutArtistException exception, Locale locale) {
    final String errorMessage =
        this.messageSource.getMessage(
            TRACK_WITHOUT_ARTIST, new Object[] {exception.getMessage()}, locale);
    log.error(errorMessage, exception);
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /* INTERNAL SERVER ERROR, UNEXPECTED, 500 */

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestMessage> handleException(Exception exception, Locale locale) {
    final String errorMessage = this.messageSource.getMessage(UNEXPECTED_ERROR, null, locale);
    log.error(errorMessage, exception);
    return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
