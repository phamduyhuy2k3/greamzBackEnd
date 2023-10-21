package com.greamz.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate(){



        return new RestTemplate(getClientHttpRequestFactory());
    }
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000; // Set your desired timeout

        // Use the HttpComponentsClientHttpRequestFactory for automatic redirects
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);

        return factory;
    }
    @Bean
    public Cloudinary cloudinary() {

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dtreuuola",
                "api_key", "118212349948963",
                "api_secret", "li_dlSoQUP5foS8Wbg3FRdh5e9I",
                "secure", true));
    }

}
