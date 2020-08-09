package com.loyalty.challenge.model;

import java.io.Serializable;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.loyalty.challenge.deserializer.CountryDeserializer;

@JsonDeserialize(using = CountryDeserializer.class)
@Persistent
@Document("country")
public class Country implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	private ObjectId id;
	
	@Indexed(name = "countryCodeIndex", expireAfterSeconds = 18000)
	private String countryCode;
	
	private String isoCode;
	
	@DBRef
	private List<Currency> currencies;
	
	private Map<String, String> languages;
	
	private String countryName;
	
	private double lat;
	
	private double lng;
	
	private double distance;
	
	private List<ZoneOffset> timeZones;
	
	public List<Currency> getCurrencies() {
		return currencies;
	}
	
	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public Map<String, String> getLanguages() {
		return languages;
	}
	
	public void setLanguages(Map<String, String> languages) {
		this.languages = languages;
	}
	
	public List<ZoneOffset> getTimeZones() {
		return timeZones;
	}
	
	public void setTimeZones(List<ZoneOffset> timeZones) {
		this.timeZones = timeZones;
	}
	
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public String getIsoCode() {
		return isoCode;
	}
	
	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
}
