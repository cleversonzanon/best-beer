package beer.me.bestbeer.service;

import beer.me.bestbeer.dto.BeerDto;
import beer.me.bestbeer.dto.BeerSearchDto;
import beer.me.bestbeer.dto.BeerUpdateDto;
import beer.me.bestbeer.dto.IdealBeerDto;
import beer.me.bestbeer.entity.Beer;
import beer.me.bestbeer.exception.BeerNotFoundException;
import beer.me.bestbeer.repository.BeerRepository;
import beer.me.bestbeer.specifications.BeerSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

  private final BeerRepository beerRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<Beer> findAll() {
    return this.beerRepository.findAll();
  }

  @Override
  public IdealBeerDto findBy(final BeerSearchDto dto) {
    List<Beer> beerList =
        this.beerRepository.findAll(
            BeerSpecifications.search(dto.getTemperature()), Sort.by("beerStyle"));
    return null;
  }

  @Override
  public void create(final BeerDto dto) {
    final Beer beer = this.modelMapper.map(dto, Beer.class);
    this.beerRepository.save(beer);
  }

  @Override
  public void update(final BeerUpdateDto dto) {
    final Beer beer = this.modelMapper.map(dto, Beer.class);
    this.beerRepository.save(beer);
  }

  @Override
  public String delete(final Long id) {
    final Beer beer =
        this.beerRepository
            .findById(id)
            .orElseThrow(() -> new BeerNotFoundException(Long.toString(id)));
    this.beerRepository.delete(beer);
    return beer.getBeerStyle();
  }
}