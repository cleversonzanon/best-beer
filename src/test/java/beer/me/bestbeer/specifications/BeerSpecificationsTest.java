package beer.me.bestbeer.specifications;

import beer.me.bestbeer.entity.Beer;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.Assert.assertNotNull;

public class BeerSpecificationsTest {

  @Test
  public void givenTemperature_whenSearch_thenReturnSpecification() {
    // GIVEN
    Float temperature = -2F;
    // WHEN
    Specification<Beer> specification = BeerSpecifications.search(temperature);
    // THEN
    assertNotNull(specification);
  }
}
