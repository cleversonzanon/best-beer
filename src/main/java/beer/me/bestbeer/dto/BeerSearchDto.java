package beer.me.bestbeer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/** DTO used for mapping the request body when searching for an ideal beer by temperature */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeerSearchDto {

  @NotNull private Float temperature;
}
