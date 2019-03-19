package beer.me.bestbeer.controller;

import beer.me.bestbeer.dto.BeerDto;
import beer.me.bestbeer.dto.BeerSearchDto;
import beer.me.bestbeer.dto.BeerUpdateDto;
import beer.me.bestbeer.dto.IdealBeerDto;
import beer.me.bestbeer.entity.Beer;
import beer.me.bestbeer.service.BeerService;
import beer.me.bestbeer.util.RestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("beer")
public class BeerController {

  private final BeerService beerService;

  @GetMapping
  public List<Beer> findAll() {
    return this.beerService.findAll();
  }

  @GetMapping("search")
  public IdealBeerDto find(@RequestBody @Valid final BeerSearchDto dto) {
    return null;
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
