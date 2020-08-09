package com.loyalty.challenge.transformer;

import org.springframework.stereotype.Component;
import com.loyalty.challenge.dto.HistoricalDistanceInvocationDTO;
import com.loyalty.challenge.model.HistoricalDistanceInvocation;

@Component
public class HistoricalDistanceInvocationTransformer {
	
	public HistoricalDistanceInvocationDTO transform(HistoricalDistanceInvocation historicalDistanceInvocation) {
		HistoricalDistanceInvocationDTO historicalDistanceInvocationDTO = new HistoricalDistanceInvocationDTO();
		historicalDistanceInvocationDTO.setAverageDistance(String.format("%s kms", this.resolveDoubleValue(historicalDistanceInvocation.getAverageDistance())));
		historicalDistanceInvocationDTO.setMaxDistance(String.format("%s kms", this.resolveDoubleValue(historicalDistanceInvocation.getMaxDistance())));
		historicalDistanceInvocationDTO.setMinDistance(String.format("%s kms", this.resolveDoubleValue(historicalDistanceInvocation.getMinDistance())));
		return historicalDistanceInvocationDTO;
	}
	
	private int resolveDoubleValue(Double value) {
		if (value != null) {
			return value.intValue();
		}
		return 0;
	}
}
