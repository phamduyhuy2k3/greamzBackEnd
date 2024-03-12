package com.greamz.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration

public class AppConfig implements WebMvcConfigurer {
    @Value("${application.cloudinary.cloud-name}")
    private String CLOUDINARY_CLOUD_NAME;
    @Value("${application.cloudinary.api-key}")
    private String CLOUDINARY_API_KEY;
    @Value("${application.cloudinary.api-secret}")
    private String CLOUDINARY_API_SECRET;
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(getClientHttpRequestFactory());
    }
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000; // Set your desired timeout

        // Use the HttpComponentsClientHttpRequestFactory for automatic redirects
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setConnectionRequestTimeout(timeout);

        return factory;
    }
    @Bean
    public Cloudinary cloudinary() {

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUDINARY_CLOUD_NAME,
                "api_key", CLOUDINARY_API_KEY,
                "api_secret", CLOUDINARY_API_SECRET,
                "secure", true));
    }
    @Bean
    public RedirectStrategy redirectStrategy(){
        return new DefaultRedirectStrategy();
    }
//    @Bean
//    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//        return (builder) -> builder
//                .withCacheConfiguration("orderCache",
//                        cacheConfiguration(Duration.ofMinutes(1)))
//                .cacheDefaults(
//                        cacheConfiguration(Duration.ofMinutes(60)));
//    }
//    public RedisCacheConfiguration cacheConfiguration(Duration duration) {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .disableCachingNullValues() // Disable caching null values
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair.fromSerializer(
//                                new Jackson2JsonRedisSerializer<>(Object.class)
//                        )
//                )
//                .serializeKeysWith(
//                        RedisSerializationContext.SerializationPair.fromSerializer(
//                                new StringRedisSerializer()
//                        )
//                )
//                .entryTtl(duration);
//    }


}
