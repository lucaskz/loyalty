package com.loyalty.challenge.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Persistent
@Document("tracking")
public class Tracking implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private ObjectId id;
	
	@Indexed(name = "ipIndex", expireAfterSeconds = 600)
	private String ip;
	
	@DBRef
	private Country country;
	
	@Transient
	private OffsetDateTime date;
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public OffsetDateTime getDate() {
		return date;
	}
	
	public void setDate(OffsetDateTime date) {
		this.date = date;
	}
	
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
	}
}
