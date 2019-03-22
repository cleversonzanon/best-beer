package beer.me.bestbeer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** Swagger controller documentation configuration */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  /**
   * Configures the Swagger options
   *
   * @return the configured docket
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(
            RequestHandlerSelectors.basePackage(
                "beer.me.bestbeer.controller")) // Set the controller base package
        .paths(PathSelectors.any())
        .build();
  }
}
