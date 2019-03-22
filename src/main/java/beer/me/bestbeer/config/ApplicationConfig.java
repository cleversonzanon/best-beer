package beer.me.bestbeer.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** General Application configuration */
@Configuration
public class ApplicationConfig {

  /**
   * Creates the model mapper bean
   *
   * @return the model mapper bean
   */
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
