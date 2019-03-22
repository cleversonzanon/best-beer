package beer.me.bestbeer.controller;

import beer.me.bestbeer.domain.Playlist;
import beer.me.bestbeer.dto.BeerDto;
import beer.me.bestbeer.dto.BeerSearchDto;
import beer.me.bestbeer.dto.BeerUpdateDto;
import beer.me.bestbeer.dto.IdealBeerDto;
import beer.me.bestbeer.entity.Beer;
import beer.me.bestbeer.service.BeerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static beer.me.bestbeer.util.JsonUtil.toJson;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BeerController.class)
public class BeerControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private BeerService beerService;

  @Test
  public void givenBeers_whenFindAll_thenReturnBeerArray() throws Exception {
    // GIVEN
    Beer beer = Beer.builder().id(101L).beerStyle("Imperial Stout").build();
    List<Beer> beerList = Collections.singletonList(beer);
    // WHEN
    given(this.beerService.findAll()).willReturn(beerList);
    // THEN
    this.mvc
        .perform(get("/beer").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].beerStyle", is(beer.getBeerStyle())));
  }

  @Test
  public void givenValidDto_whenFind_thenReturnIdealBeerArray() throws Exception {
    // MOCKS
    IdealBeerDto idealBeerDto = new IdealBeerDto("Imperial Stout", new Playlist());
    List<IdealBeerDto> result = Collections.singletonList(idealBeerDto);
    // GIVEN
    BeerSearchDto dto = new BeerSearchDto(0F);
    // WHEN
    given(this.beerService.findBy(any())).willReturn(result);
    // THEN
    this.mvc
        .perform(post("/beer/search").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].beerStyle", is(idealBeerDto.getBeerStyle())));
  }

  @Test
  public void givenInvalidDto_whenFind_thenReturnBadRequest() throws Exception {
    // GIVEN
    BeerSearchDto dto = new BeerSearchDto(null);
    // THEN
    this.mvc
        .perform(post("/beer/search").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .json(
                    "{\"content\":\"In order to perform a search, a temperature must be informed\"}"));
  }

  @Test
  public void givenValidDto_whenCreate_thenReturnOk() throws Exception {
    // GIVEN
    BeerDto dto = new BeerDto("Imperial Stout", -10F, 10F);
    // THEN
    this.mvc
        .perform(post("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isOk());
    // ASSERTIONS
    verify(this.beerService, times(1)).create(any());
    reset(this.beerService);
  }

  @Test
  public void givenDtoWithoutBeerStyle_whenCreate_thenReturnBadRequest() throws Exception {
    // GIVEN
    BeerDto dto = new BeerDto(null, -10F, 10F);
    // THEN
    this.mvc
        .perform(post("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().json("{\"content\":\"The Beer Style must be informed\"}"));
  }

  @Test
  public void givenDtoWithoutMinIdealTemperature_whenCreate_thenReturnBadRequest()
      throws Exception {
    // GIVEN
    BeerDto dto = new BeerDto("Imperial Stout", null, 10F);
    // THEN
    this.mvc
        .perform(post("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content().json("{\"content\":\"The minimal ideal temperature must be informed\"}"));
  }

  @Test
  public void givenDtoWithoutMaxIdealTemperature_whenCreate_thenReturnBadRequest()
      throws Exception {
    // GIVEN
    BeerDto dto = new BeerDto("Imperial Stout", -10F, null);
    // THEN
    this.mvc
        .perform(post("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content().json("{\"content\":\"The maximum ideal temperature must be informed\"}"));
  }

  @Test
  public void givenValidDto_whenUpdate_thenReturnOk() throws Exception {
    // GIVEN
    BeerUpdateDto dto = new BeerUpdateDto(101L, "Imperial Stout", -10F, 10F);
    // THEN
    this.mvc
        .perform(put("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isOk());
    // ASSERTIONS
    verify(this.beerService, times(1)).update(any());
    reset(this.beerService);
  }

  @Test
  public void givenDtoWithoutId_whenUpdate_thenReturnBadRequest() throws Exception {
    // GIVEN
    BeerUpdateDto dto = new BeerUpdateDto(null, "Imperial Stout", -10F, 10F);
    // THEN
    this.mvc
        .perform(put("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().json("{\"content\":\"The id must be informed\"}"));
  }

  @Test
  public void givenDtoWithoutBeerStyle_whenUpdate_thenReturnBadRequest() throws Exception {
    // GIVEN
    BeerUpdateDto dto = new BeerUpdateDto(101L, null, -10F, 10F);
    // THEN
    this.mvc
        .perform(put("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().json("{\"content\":\"The Beer Style must be informed\"}"));
  }

  @Test
  public void givenDtoWithoutMinIdealTemperature_whenUpdate_thenReturnBadRequest()
      throws Exception {
    // GIVEN
    BeerUpdateDto dto = new BeerUpdateDto(101L, "Imperial Stout", null, 10F);
    // THEN
    this.mvc
        .perform(put("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content().json("{\"content\":\"The minimal ideal temperature must be informed\"}"));
  }

  @Test
  public void givenDtoWithoutMaxIdealTemperature_whenUpdate_thenReturnBadRequest()
      throws Exception {
    // GIVEN
    BeerUpdateDto dto = new BeerUpdateDto(101L, "Imperial Stout", -10F, null);
    // THEN
    this.mvc
        .perform(put("/beer").contentType(MediaType.APPLICATION_JSON).content(toJson(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content().json("{\"content\":\"The maximum ideal temperature must be informed\"}"));
  }

  @Test
  public void givenValidId_whenDelete_thenReturnOk() throws Exception {
    // GIVEN
    Long id = 101L;
    // WHEN
    given(this.beerService.delete(any())).willReturn("Imperial Stout");
    // THEN
    this.mvc
        .perform(delete("/beer/" + Long.toString(id)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", is("Imperial Stout has been deleted")));
    // ASSERTIONS
    verify(this.beerService, times(1)).delete(any());
    reset(this.beerService);
  }

  @Test
  public void givenInvalidId_whenDelete_thenReturnInternalServerError() throws Exception {
    // GIVEN
    String id = "101Z";
    // THEN
    this.mvc
        .perform(delete("/beer/" + id).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(
            content().json("{\"content\":\"An unexpected error occurred during your request\"}"));
  }
}
