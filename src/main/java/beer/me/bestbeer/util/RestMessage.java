package beer.me.bestbeer.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/** Object used as response for request that doesn't have a DTO */
@Getter
@Setter
public class RestMessage {

  private String content;

  public RestMessage(final String content) {
    this.content = content;
  }

  public RestMessage(final List<String> content) {
    this.content = content.stream().collect(Collectors.joining(", "));
  }
}
