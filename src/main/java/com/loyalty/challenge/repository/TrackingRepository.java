package com.loyalty.challenge.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.loyalty.challenge.model.Tracking;

public interface TrackingRepository extends MongoRepository<Tracking, ObjectId> {
	
	Tracking findTopByIp(String ip);
	
}
