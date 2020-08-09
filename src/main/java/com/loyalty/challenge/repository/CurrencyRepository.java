package com.loyalty.challenge.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.loyalty.challenge.model.Currency;

public interface CurrencyRepository extends MongoRepository<Currency, ObjectId> {
}
