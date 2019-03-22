package beer.me.bestbeer.specifications;

import beer.me.bestbeer.entity.Beer;
import beer.me.bestbeer.entity.Beer_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedHashSet;
import java.util.Set;

/** The method to built specifications for the Beer entity */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeerSpecifications {

  /**
   * Build beer's search by temperature specification
   *
   * @param temperature the search temperature
   * @return the built specification
   */
  public static Specification<Beer> search(final Float temperature) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      // The set of all predicates
      Set<Predicate> predicateSet = new LinkedHashSet<>();
      // Build the sub query to be used on the where clause
      Subquery<Float> subQuery =
          buildMinimalAbsDiffSubQuery(temperature, criteriaQuery, criteriaBuilder);
      // Get the beer`s average temperature singular attribute
      Path<Float> averageTemperature = root.get(Beer_.AVERAGE_TEMPERATURE);
      // Creates an expression from the search temperature
      Expression<Float> searchTemperature = criteriaBuilder.literal(temperature);
      // Criteria difference between averageTemperature and searchTemperature
      Expression<Float> diff = criteriaBuilder.diff(averageTemperature, searchTemperature);
      // Criteria to get absolute value of diff
      Expression<Float> absDiff = criteriaBuilder.abs(diff);

      /* PREDICATES */

      // The absDiff must be equal the min found by the subQuery
      predicateSet.add(criteriaBuilder.equal(absDiff, subQuery));
      // The search temperature must be less or equal to the beer's min ideal temperature
      predicateSet.add(
          criteriaBuilder.lessThanOrEqualTo(
              root.get(Beer_.minIdealTemperature), searchTemperature));
      // The search temperature must be greater or equal to the beer's max ideal temperature
      predicateSet.add(
          criteriaBuilder.greaterThanOrEqualTo(
              root.get(Beer_.maxIdealTemperature), searchTemperature));

      // Return all predicates within the set
      return criteriaBuilder.and(predicateSet.toArray(new Predicate[0]));
    };
  }

  /**
   * Build a sub query in order to find the minimal absolute difference between the average
   * temperature and the search temperature
   *
   * @param temperature the search temperature
   * @param criteriaQuery the repository criteria query
   * @param criteriaBuilder the hibernate's criteria builder
   * @return the built sub query
   */
  private static Subquery<Float> buildMinimalAbsDiffSubQuery(
      final Float temperature,
      final CriteriaQuery<?> criteriaQuery,
      final CriteriaBuilder criteriaBuilder) {
    // Create a sub query
    Subquery<Float> subQuery = criteriaQuery.subquery(Float.class);
    // Set the from to Beer.class
    Root<Beer> beer = subQuery.from(Beer.class);
    // Get the beer`s average temperature singular attribute
    Path<Float> averageTemperature = beer.get(Beer_.AVERAGE_TEMPERATURE);
    // Creates an expression from the search temperature
    Expression<Float> searchTemperature = criteriaBuilder.literal(temperature);
    // Criteria difference between averageTemperature and searchTemperature
    Expression<Float> diff = criteriaBuilder.diff(averageTemperature, searchTemperature);
    // Criteria to get absolute value of diff
    Expression<Float> absDiff = criteriaBuilder.abs(diff);
    // Set the sub query's selection, the min absDiff found
    subQuery.select(criteriaBuilder.min(absDiff));
    // Return the built sub query
    return subQuery;
  }
}
