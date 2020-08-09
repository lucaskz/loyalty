package com.loyalty.challenge.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.loyalty.challenge.model.Country;

public interface CountryRepository extends MongoRepository<Country, ObjectId> {
	
	Country findTopByCountryCode(String countryCode);
}
