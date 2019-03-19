package beer.me.bestbeer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Getter
@Setter
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

  @PrePersist
  public void prePersist() {
    this.calculateAndSetAverageTemperature();
  }

  @PreUpdate
  public void preUpdate() {
    this.calculateAndSetAverageTemperature();
  }

  private void calculateAndSetAverageTemperature() {
    float absoluteDifference =
        Math.abs(this.minIdealTemperature) + Math.abs(this.maxIdealTemperature);
    float halfway = absoluteDifference / 2;
    this.averageTemperature = this.minIdealTemperature + halfway;
  }
}
