package beer.me.bestbeer.dto;

import beer.me.bestbeer.domain.Playlist;
import lombok.*;

/** DTO used as response for the ideal beer by temperature search */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdealBeerDto {

  private String beerStyle;
  private Playlist playlist;
}
