package beer.me.bestbeer.service;

import beer.me.bestbeer.domain.Playlist;
import beer.me.bestbeer.dto.BeerDto;
import beer.me.bestbeer.dto.BeerSearchDto;
import beer.me.bestbeer.dto.BeerUpdateDto;
import beer.me.bestbeer.dto.IdealBeerDto;
import beer.me.bestbeer.entity.Beer;
import beer.me.bestbeer.exception.BeerNotFoundException;
import beer.me.bestbeer.repository.BeerRepository;
import beer.me.bestbeer.service.impl.BeerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class BeerServiceTest {

  // Service
  private BeerService beerService;
  // Mocks
  @MockBean private BeerRepository beerRepository;
  @MockBean private SpotifyService spotifyService;
  @Mock private ModelMapper modelMapper;
  // Properties
  private Beer beer;
  private List<Beer> beerList = new ArrayList<>();

  @Before
  public void setup() {
    // Service
    this.beerService =
        new BeerServiceImpl(this.beerRepository, this.modelMapper, this.spotifyService);
    // Entity
    this.beer =
        Beer.builder()
            .id(101L)
            .beerStyle("Imperial Stout")
            .minIdealTemperature(-10F)
            .maxIdealTemperature(10F)
            .averageTemperature(0F)
            .build();
    // List of beers
    this.beerList.add(beer);
  }

  @Test
  public void whenFindAll_thenResultShouldNotBeEmpty() {
    // WHEN
    // Result stub
    when(this.beerRepository.findAll()).thenReturn(this.beerList);
    // Method call
    List<Beer> result = this.beerService.findAll();
    // THEN
    assertFalse(CollectionUtils.isEmpty(result));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void givenValidDto_whenFindByBeerSearchDto_thenResultMustBeValid() {
    // GIVEN
    BeerSearchDto dto = new BeerSearchDto(0F);
    // WHEN
    // Find all by temperature stub
    when(this.beerRepository.findAll(any(Specification.class), any(Sort.class)))
        .thenReturn(this.beerList);
    // Find play list by beer style stub
    when(this.spotifyService.findPlaylistByBeerStyle(any(String.class))).thenReturn(new Playlist());
    // Method call
    List<IdealBeerDto> result = this.beerService.findBy(dto);
    // THEN
    // For each assert
    result.forEach(
        idealBeerDto -> {
          assertEquals("Imperial Stout", idealBeerDto.getBeerStyle());
          assertNotNull(idealBeerDto.getPlaylist());
        });
  }

  @Test
  public void givenValidDto_whenCreate_thenSaveMustBeCalledOnce() {
    // GIVEN
    BeerDto dto = new BeerDto("Imperial Stout", -10F, 10F);
    // WHEN
    // Entity mapping stub
    when(this.modelMapper.map(dto, Beer.class)).thenReturn(this.beer);
    // Method call
    this.beerService.create(dto);
    // THEN
    verify(this.beerRepository, times(1)).save(this.beer);
  }

  @Test
  public void givenValidDto_whenUpdate_thenSaveMustBeCalledOnce() {
    // GIVEN
    BeerUpdateDto dto = new BeerUpdateDto(101L, "Imperial Stout", -10F, 10F);
    // WHEN
    // Entity mapping stub
    when(this.modelMapper.map(dto, Beer.class)).thenReturn(this.beer);
    // Method call
    this.beerService.update(dto);
    // THEN
    verify(this.beerRepository, times(1)).save(this.beer);
  }

  @Test
  public void givenValidId_whenDelete_thenMustCallDeleteOnceAndReturnBeerStyle() {
    // GIVEN
    Long id = this.beer.getId();
    // WHEN
    // Find by id stub
    when(this.beerRepository.findById(id)).thenReturn(Optional.of(this.beer));
    // Method call
    String result = this.beerService.delete(id);
    // THEN
    // Call delete once
    verify(this.beerRepository, times(1)).delete(this.beer);
    // The beer style deleted
    assertEquals("Imperial Stout", result);
  }

  @Test(expected = BeerNotFoundException.class)
  public void givenInvalidId_whenDelete_thenMustThrowException() {
    // GIVEN
    Long id = 109L;
    // WHEN
    // Find by id stub
    when(this.beerRepository.findById(id)).thenReturn(Optional.empty());
    // Method call
    this.beerService.delete(id);
  }

  @Test(expected = BeerNotFoundException.class)
  public void givenNullId_whenDelete_thenMustThrowException() {
    // Method call
    this.beerService.delete(null);
  }
}
