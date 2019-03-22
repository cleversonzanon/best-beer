package beer.me.bestbeer.service;

import beer.me.bestbeer.dto.BeerDto;
import beer.me.bestbeer.dto.BeerSearchDto;
import beer.me.bestbeer.dto.BeerUpdateDto;
import beer.me.bestbeer.dto.IdealBeerDto;
import beer.me.bestbeer.entity.Beer;

import javax.validation.constraints.NotNull;
import java.util.List;

/** Service related to Beer entity. */
public interface BeerService {

  /**
   * Find all beer styles
   *
   * @return the list of beer styles
   */
  List<Beer> findAll();

  /**
   * Find the ideal beer style given a temperature
   *
   * @param dto the temperature dto
   * @return the list of ideal beer styles
   */
  List<IdealBeerDto> findBy(@NotNull final BeerSearchDto dto);

  /**
   * Create a new beer style
   *
   * @param dto the beer dto
   */
  void create(final BeerDto dto);

  /**
   * Update a beer style info
   *
   * @param dto the beer update dto
   */
  void update(final BeerUpdateDto dto);

  /**
   * Delete a beer style by id
   *
   * @param id the id of the beer to be deleted
   * @return the name of the deleted beer
   */
  String delete(final Long id);
}
