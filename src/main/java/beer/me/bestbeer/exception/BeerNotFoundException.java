package beer.me.bestbeer.exception;

/** Exception thrown when a Beer entity search is not found */
public class BeerNotFoundException extends RuntimeException {

  public BeerNotFoundException() {
    super();
  }

  public BeerNotFoundException(final String message) {
    super(message);
  }
}
