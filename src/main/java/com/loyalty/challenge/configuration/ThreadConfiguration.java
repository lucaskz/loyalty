package com.loyalty.challenge.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import com.loyalty.challenge.model.Tracking;

/**
 * ThreadPool configuration. Used in {@link com.loyalty.challenge.service.TransactionHistoryService#recordTransaction(Tracking)}
 *
 * @author Lucas Kaseta
 */

@Configuration
@EnableAsync
@Profile("!test")
public class ThreadConfiguration {
	
	private static final int CORE_POOL_SIZE = 10;
	private static final int MAX_POOL_SIZE = 400;
	public static final String TRANSACTION_RECORDER_THREAD_POOL_TASK_EXECUTOR = "transactionRecorderThreadPoolTaskExecutor";
	
	@Bean(name = TRANSACTION_RECORDER_THREAD_POOL_TASK_EXECUTOR)
	public Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setThreadNamePrefix(TRANSACTION_RECORDER_THREAD_POOL_TASK_EXECUTOR);
		executor.initialize();
		return executor;
	}
}
