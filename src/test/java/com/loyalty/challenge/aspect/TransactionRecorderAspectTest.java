package com.loyalty.challenge.aspect;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.loyalty.challenge.annotation.TransactionRecorder;
import com.loyalty.challenge.model.Tracking;
import com.loyalty.challenge.service.TransactionHistoryService;

@SpringBootTest
class TransactionRecorderAspectTest {
	
	@Mock
	private TransactionHistoryService transactionRecorderService;
	
	private TransactionRecorderAspect transactionRecorderAspect;
	
	@BeforeEach
	void setup() {
		this.transactionRecorderAspect = new TransactionRecorderAspect(transactionRecorderService);
	}
	
	@Test
	void testSuccessUserOperationSave_Succeed() {
		Tracking tracking = new Tracking();
		this.transactionRecorderAspect.afterMethodService(new TransactionRecorder() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return TransactionRecorder.class;
			}
		}, tracking);
		
		verify(transactionRecorderService, times(1)).recordTransaction(tracking);
	}
}
