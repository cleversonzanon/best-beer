package beer.me.bestbeer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BeerDto {

  @NotEmpty private String beerStyle;
  @NotNull private Float minIdealTemperature;
  @NotNull private Float maxIdealTemperature;
}
