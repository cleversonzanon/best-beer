package beer.me.bestbeer.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** THe Beer entity */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beer {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false)
  private String beerStyle;

  @Column(nullable = false)
  private Float minIdealTemperature;

  @Column(nullable = false)
  private Float maxIdealTemperature;

  @Column(nullable = false)
  private Float averageTemperature;
}
