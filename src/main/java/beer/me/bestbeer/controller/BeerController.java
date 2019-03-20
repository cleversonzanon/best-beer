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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("beer")
public class BeerController {

  private final BeerService beerService;

  @GetMapping
  public List<Beer> findAll() {
    return this.beerService.findAll();
  }

  @PostMapping("search")
  public List<IdealBeerDto> find(@RequestBody @Valid final BeerSearchDto dto) {
    return this.beerService.findBy(dto);
  }

  @PostMapping
  public void create(@RequestBody @Valid final BeerDto dto) {
    this.beerService.create(dto);
  }

  @PutMapping
  public void update(@RequestBody @Valid final BeerUpdateDto dto) {
    this.beerService.update(dto);
  }

  @DeleteMapping("{id}")
  public RestMessage delete(@PathVariable final Long id) {
    String deletedBeer = this.beerService.delete(id) + "has been deleted";
    return new RestMessage(deletedBeer);
  }
}
