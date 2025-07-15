package br.com.pedromonteiro.order_service_api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.pedromonteiro.order_service_api.decoder.RetrieveMessageErrorDecoder;
import feign.codec.ErrorDecoder;

@Configuration
public class FeignConfigs {
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new RetrieveMessageErrorDecoder();
    }
}
