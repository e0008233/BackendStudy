package day.day.up.programming.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class swaggerConfig {

    @Bean
    public Docket swaggerPlugin() {
        List<Parameter> parameters = new ArrayList<>();
        Parameter token = new ParameterBuilder()
                .name("X-Token")
                .description("login token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .build();
        parameters.add(token);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("day.day.up"))
                .build()
                .globalOperationParameters(parameters);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Api list")
                .description("Interface description")
                .version("1.0")
                .build();
    }
}
