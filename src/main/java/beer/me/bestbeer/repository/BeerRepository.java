package beer.me.bestbeer.repository;

import beer.me.bestbeer.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/** Beer's entity JPA repository */
public interface BeerRepository extends JpaRepository<Beer, Long>, JpaSpecificationExecutor<Beer> {}
