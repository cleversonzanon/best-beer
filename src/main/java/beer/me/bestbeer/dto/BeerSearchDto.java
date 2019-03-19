package beer.me.bestbeer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BeerSearchDto {

  @NotNull private Float temperature;
}
