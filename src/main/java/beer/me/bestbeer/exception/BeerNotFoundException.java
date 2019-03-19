package beer.me.bestbeer.exception;

public class BeerNotFoundException extends RuntimeException {

  public BeerNotFoundException(final String message) {
    super(message);
  }
}
