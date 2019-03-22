package beer.me.bestbeer.domain;

import lombok.*;

import java.util.List;

/** Playlist domain object */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

  private String name;
  private List<Track> tracks;
}
