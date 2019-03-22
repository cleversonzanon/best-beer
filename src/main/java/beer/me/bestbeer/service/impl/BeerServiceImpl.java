package beer.me.bestbeer.service.impl;

import beer.me.bestbeer.domain.Playlist;
import beer.me.bestbeer.dto.BeerDto;
import beer.me.bestbeer.dto.BeerSearchDto;
import beer.me.bestbeer.dto.BeerUpdateDto;
import beer.me.bestbeer.dto.IdealBeerDto;
import beer.me.bestbeer.entity.Beer;
import beer.me.bestbeer.exception.BeerNotFoundException;
import beer.me.bestbeer.repository.BeerRepository;
import beer.me.bestbeer.service.BeerService;
import beer.me.bestbeer.service.SpotifyService;
import beer.me.bestbeer.specifications.BeerSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** The Class BeerServiceImpl. */
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

  private final BeerRepository beerRepository;
  private final ModelMapper modelMapper;
  private final SpotifyService spotifyService;

  /**
   * Find all beer styles
   *
   * @return the list of beer styles
   */
  @Override
  public List<Beer> findAll() {
    return this.beerRepository.findAll();
  }

  /**
   * Find the ideal beer style given a temperature
   *
   * @param dto the temperature dto
   * @return the list of ideal beer styles
   */
  @Override
  public List<IdealBeerDto> findBy(final BeerSearchDto dto) {
    // The beer search specification
    Specification<Beer> specification = BeerSpecifications.search(dto.getTemperature());
    // Find the list of beers by the specification and sorted alphabetically
    List<Beer> beerList = this.beerRepository.findAll(specification, Sort.by("beerStyle"));
    // Build a DTO for each beer found
    return beerList.stream()
        .map(
            beer -> {
              // Find a playlist by the beer style
              Playlist playlist = this.spotifyService.findPlaylistByBeerStyle(beer.getBeerStyle());
              // Build the DTO
              return IdealBeerDto.builder()
                  .beerStyle(beer.getBeerStyle())
                  .playlist(playlist)
                  .build();
            })
        .collect(Collectors.toList());
  }

  /**
   * Create a new beer style
   *
   * @param dto the beer dto
   */
  @Override
  public void create(final BeerDto dto) {
    // Map the dto to entity
    final Beer beer = this.modelMapper.map(dto, Beer.class);
    // Calculate and set the average temperature for the entity
    this.calculateAndSetAverageTemperature(beer);
    // Persist the new entity
    this.beerRepository.save(beer);
  }

  /**
   * Update a beer style info
   *
   * @param dto the beer update dto
   */
  @Override
  public void update(final BeerUpdateDto dto) {
    // Map the dto to entity
    final Beer beer = this.modelMapper.map(dto, Beer.class);
    // Calculate and set the average temperature for the entity
    this.calculateAndSetAverageTemperature(beer);
    // Persist the changes
    this.beerRepository.save(beer);
  }

  /**
   * Delete a beer style by id
   *
   * @param id the id of the beer to be deleted
   * @return the name of the deleted beer
   */
  @Override
  public String delete(final Long id) {
    // If null, throw exception
    if (id == null) {
      throw new BeerNotFoundException();
    }
    // Find by id, in order to retrieve the name, or throw an exception when not found
    final Beer beer =
        this.beerRepository
            .findById(id)
            .orElseThrow(() -> new BeerNotFoundException(Long.toString(id)));
    // Delete the entity
    this.beerRepository.delete(beer);
    // Return the beer name
    return beer.getBeerStyle();
  }

  /** Calculate and set the average temperature for the entity */
  private void calculateAndSetAverageTemperature(final Beer beer) {
    // Get the amount of degrees between the min and max temperature
    float absoluteDifference =
        Math.abs(beer.getMinIdealTemperature()) + Math.abs(beer.getMaxIdealTemperature());
    // The average of the absoluteDifference
    float halfway = absoluteDifference / 2;
    // Set the average degree between the min and max temperature, min plus the halfway result. Max
    // minus the halfway would result in the same value
    beer.setAverageTemperature(beer.getMinIdealTemperature() + halfway);
  }
}
