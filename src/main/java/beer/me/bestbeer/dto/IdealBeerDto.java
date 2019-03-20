package beer.me.bestbeer.dto;

import beer.me.bestbeer.domain.Playlist;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdealBeerDto {

  private String beerStyle;
  private Playlist playlist;
}
