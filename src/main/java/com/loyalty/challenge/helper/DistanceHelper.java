package com.loyalty.challenge.helper;

/**
 * Calculates the distance between two points of the earth, where one point is always static
 * Each point is made of a latitude and longitude.
 * The distances is measured in Kilometers.
 *
 * @author Lucas Kaseta
 */

public class DistanceHelper {
	
	private static final double R = 6371.0088;// Earth's radius Km
	private static final double INITIAL_LATITUDE = -34.6083;
	private static final double INITIAL_LONGITUDE = -58.3712;
	
	public static Double measureDistance(double lat, double lng) {
		Double latDistance = toRad(lat - INITIAL_LATITUDE);
		Double lonDistance = toRad(lng - INITIAL_LONGITUDE);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
				Math.cos(toRad(INITIAL_LATITUDE)) * Math.cos(toRad(lat)) *
						Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}
	
	private static Double toRad(Double value) {
		return value * Math.PI / 180;
	}
}
