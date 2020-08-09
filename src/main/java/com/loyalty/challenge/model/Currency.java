package com.loyalty.challenge.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Persistent
@Document("currency")
public class Currency implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private ObjectId id;
	
	@Indexed(name = "currencyCodeIndex", expireAfterSeconds = 18000)
	private String code;
	
	private String name;
	
	@Transient
	private BigDecimal quote;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getQuote() {
		return quote;
	}
	
	public void setQuote(BigDecimal quote) {
		this.quote = quote;
	}
	
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
	}
}
