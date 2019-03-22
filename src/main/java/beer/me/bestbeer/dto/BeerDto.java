package beer.me.bestbeer.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/** DTO used for mapping the request body when creating a new Beer entity */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeerDto {

  @NotEmpty private String beerStyle;
  @NotNull private Float minIdealTemperature;
  @NotNull private Float maxIdealTemperature;
}
