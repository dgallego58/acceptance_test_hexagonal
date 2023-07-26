package co.com.bancolombia.config;

import co.com.bancolombia.usecase.EventAnswer;
import co.com.bancolombia.usecase.NotifySubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {


    @Bean
    public NotifySubscriber notifySubscriber() {
        return NotifySubscriber.createChain();
    }

    @Bean
    public EventAnswer eventAnswer(NotifySubscriber notifySubscriber) {
        return EventAnswer.of(notifySubscriber);
    }

}
