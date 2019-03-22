package beer.me.bestbeer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/** DTO used for mapping the request body when updating a existing Beer entity */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeerUpdateDto {

  @NotNull private Long id;
  @NotEmpty private String beerStyle;
  @NotNull private Float minIdealTemperature;
  @NotNull private Float maxIdealTemperature;
}
