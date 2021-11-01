package com.example.studentmanagement.deadline.repositories;

import com.example.studentmanagement.deadline.entities.Deadline;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
@AllArgsConstructor
public class DeadlineRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Deadline> addDeadline(Deadline deadline) {
        return reactiveMongoTemplate.save(deadline);
    }

    public Mono<Deadline> getDeadlineById(String id) {
        return reactiveMongoTemplate.findById(id, Deadline.class);
    }

    public Mono<Deadline> deleteDeadlineById(String id) {
        val criteria = Criteria.where(Deadline.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.findAndRemove(query, Deadline.class);
    }

    public Mono<Boolean> checkIfDeadlineIsAvailable(String deadlineId) {
        val currentTime = Instant.now();

        val criteria1 = Criteria.where(Deadline.ID).is(deadlineId);
        val criteria2 = Criteria.where(Deadline.STARTDATE).lte(currentTime);
        val criteria3 = Criteria.where(Deadline.ENDDATE).gte(currentTime);
        val criteria = new Criteria().andOperator(criteria1, criteria2, criteria3);

        val query = Query.query(criteria);

        return reactiveMongoTemplate.exists(query, Deadline.class);
    }


}
