package academy.everyonecodes.java.service;

import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
