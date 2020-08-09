package com.loyalty.challenge.configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.resilience4j.common.ratelimiter.configuration.RateLimiterConfigCustomizer;

/**
 * Main application's config bean definition.
 *
 * @author Lucas Kaseta
 */

@Configuration
@EnableCaching
public class AppConfiguration implements WebMvcConfigurer {
	
	private static final long CURRENCY_CACHE_TTL = 5;
	private static final long COUNTRY_CACHE_TTL = 60;
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}
	
	@Bean
	@Primary
	public CacheManager currencyCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager("currencies");
		cacheManager.setCaffeine(Caffeine.newBuilder()
				.initialCapacity(100)
				.maximumSize(500)
				.expireAfterAccess(CURRENCY_CACHE_TTL, TimeUnit.MINUTES)
				.recordStats());
		return cacheManager;
	}
	
	@Bean
	public CacheManager countryCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager("countries");
		cacheManager.setCaffeine(Caffeine.newBuilder()
				.initialCapacity(100)
				.maximumSize(400)
				.expireAfterAccess(COUNTRY_CACHE_TTL, TimeUnit.MINUTES)
				.recordStats());
		return cacheManager;
	}
	
	@Bean
	public RateLimiterConfigCustomizer traceServiceRateLimiterCustomizer() {
		return RateLimiterConfigCustomizer.of("traceServiceRateLimiter", builder -> builder.limitForPeriod(1000).limitRefreshPeriod(Duration.ofMillis(500)));
	}
	
	@Bean
	public RateLimiterConfigCustomizer transactionHistoryServiceRateLimiterCustomizer() {
		return RateLimiterConfigCustomizer.of("transactionHistoryServiceRateLimiter", builder -> builder.limitForPeriod(1000).limitRefreshPeriod(Duration.ofMillis(500)));
	}
	
	@Bean
	public RateLimiterConfigCustomizer countryClientRateLimiterCustomizer() {
		return RateLimiterConfigCustomizer.of("countryClientRateLimiter", builder -> builder.limitForPeriod(1000).limitRefreshPeriod(Duration.ofMillis(500)));
	}
	
	@Bean
	public RateLimiterConfigCustomizer ipClientRateLimiterCustomizer() {
		return RateLimiterConfigCustomizer.of("ipClientRateLimiter", builder -> builder.limitForPeriod(1000).limitRefreshPeriod(Duration.ofMillis(500)));
	}
	
	@Bean
	public RateLimiterConfigCustomizer currencyClientRateLimiterCustomizer() {
		return RateLimiterConfigCustomizer.of("currencyClientRateLimiter", builder -> builder.limitForPeriod(1000).limitRefreshPeriod(Duration.ofMillis(500)));
	}
}