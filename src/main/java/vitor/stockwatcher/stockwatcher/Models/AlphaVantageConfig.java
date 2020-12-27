package vitor.stockwatcher.stockwatcher.Models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:sensitive.properties")
public class AlphaVantageConfig {
    
    @Value("${alphavantage_api_key}")
    private String api_key;

    public String getApiKey(){
        return this.api_key;
    }
}
