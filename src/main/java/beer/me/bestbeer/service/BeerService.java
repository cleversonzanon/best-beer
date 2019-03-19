package beer.me.bestbeer.service;

import beer.me.bestbeer.dto.BeerDto;
import beer.me.bestbeer.dto.BeerSearchDto;
import beer.me.bestbeer.dto.BeerUpdateDto;
import beer.me.bestbeer.dto.IdealBeerDto;
import beer.me.bestbeer.entity.Beer;

import java.util.List;

public interface BeerService {

  List<Beer> findAll();

  IdealBeerDto findBy(final BeerSearchDto dto);

  void create(final BeerDto dto);

  void update(final BeerUpdateDto dto);

  String delete(final Long id);
}
