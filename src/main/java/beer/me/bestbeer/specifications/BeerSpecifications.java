package beer.me.bestbeer.specifications;

import beer.me.bestbeer.entity.Beer;
import beer.me.bestbeer.entity.Beer_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeerSpecifications {

  public static Specification<Beer> search(final Float temperature) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Set<Predicate> predicateSet = new LinkedHashSet<>();

      Subquery<Float> subQuery = buildSubQuery(temperature, criteriaQuery, criteriaBuilder);

      Path<Float> averageTemperature = root.get(Beer_.AVERAGE_TEMPERATURE);
      Expression<Float> searchTemperature = criteriaBuilder.literal(temperature);
      Expression<Float> diff = criteriaBuilder.diff(averageTemperature, searchTemperature);
      Expression<Float> absDiff = criteriaBuilder.abs(diff);

      predicateSet.add(criteriaBuilder.equal(absDiff, subQuery));
      predicateSet.add(
          criteriaBuilder.lessThanOrEqualTo(
              root.get(Beer_.minIdealTemperature), searchTemperature));
      predicateSet.add(
          criteriaBuilder.greaterThanOrEqualTo(
              root.get(Beer_.maxIdealTemperature), searchTemperature));

      return criteriaBuilder.equal(absDiff, subQuery);
    };
  }

  private static Subquery<Float> buildSubQuery(
      Float temperature, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

    Subquery<Float> subQuery = criteriaQuery.subquery(Float.class);

    Root<Beer> beer = subQuery.from(Beer.class);

    Path<Float> averageTemperature = beer.get(Beer_.AVERAGE_TEMPERATURE);
    Expression<Float> searchTemperature = criteriaBuilder.literal(temperature);
    Expression<Float> diff = criteriaBuilder.diff(averageTemperature, searchTemperature);
    Expression<Float> absDiff = criteriaBuilder.abs(diff);
    subQuery.select(criteriaBuilder.min(absDiff));
    return subQuery;
  }
}
