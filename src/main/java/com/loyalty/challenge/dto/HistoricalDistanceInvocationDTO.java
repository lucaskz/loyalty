package com.loyalty.challenge.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Represents the statistical/historical trackings made by the application's clients.
 *
 * @author Lucas Kaseta
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HistoricalDistanceInvocationDTO {
	
	private String averageDistance;
	private String maxDistance;
	private String minDistance;
	
	public String getAverageDistance() {
		return averageDistance;
	}
	
	public void setAverageDistance(String averageDistance) {
		this.averageDistance = averageDistance;
	}
	
	public String getMaxDistance() {
		return maxDistance;
	}
	
	public void setMaxDistance(String maxDistance) {
		this.maxDistance = maxDistance;
	}
	
	public String getMinDistance() {
		return minDistance;
	}
	
	public void setMinDistance(String minDistance) {
		this.minDistance = minDistance;
	}
}
