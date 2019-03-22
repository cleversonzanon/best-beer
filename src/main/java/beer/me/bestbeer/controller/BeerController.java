package beer.me.bestbeer.controller;

import beer.me.bestbeer.dto.BeerDto;
import beer.me.bestbeer.dto.BeerSearchDto;
import beer.me.bestbeer.dto.BeerUpdateDto;
import beer.me.bestbeer.dto.IdealBeerDto;
import beer.me.bestbeer.entity.Beer;
import beer.me.bestbeer.service.BeerService;
import beer.me.bestbeer.util.RestMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/** Controller for actions related to Beer entity */
@RestController
@RequiredArgsConstructor
@RequestMapping("beer")
public class BeerController {

  private final BeerService beerService;

  /**
   * Find all beers
   *
   * @return the list of all beers
   */
  @GetMapping
  public List<Beer> findAll() {
    return this.beerService.findAll();
  }

  /**
   * Find the ideal beer given a temperature
   *
   * @param dto the DTO containing the search temperature
   * @return the list of ideal beers and a respective playlist
   */
  @PostMapping("search")
  public List<IdealBeerDto> find(@RequestBody @Valid final BeerSearchDto dto) {
    return this.beerService.findBy(dto);
  }

  /**
   * Creates a new beer style
   *
   * @param dto the dto of the beer to be created
   */
  @PostMapping
  public void create(@RequestBody @Valid final BeerDto dto) {
    this.beerService.create(dto);
  }

  /**
   * Updates and existing beer style
   *
   * @param dto the dto of the beer to be updated
   */
  @PutMapping
  public void update(@RequestBody @Valid final BeerUpdateDto dto) {
    this.beerService.update(dto);
  }

  /**
   * Deletes an existing beer style by id
   *
   * @param id the id of the beer style to be deleted
   * @return the rest message of success
   */
  @DeleteMapping("{id}")
  public RestMessage delete(@PathVariable final Long id) {
    // Append the deleted beer name to the message
    String deletedBeer = this.beerService.delete(id) + " has been deleted";
    // Return the success message
    return new RestMessage(deletedBeer);
  }
}
