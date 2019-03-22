package beer.me.bestbeer.domain;

import lombok.*;

/** Track domain object */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Track {

  private String name;
  private String artist;
  private String link;
}
