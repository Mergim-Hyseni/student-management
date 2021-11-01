package com.example.studentmanagement.university.repositories;

import com.example.studentmanagement.university.entities.University;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
@AllArgsConstructor
public class UniversityRepo {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<University> addUniversity(University university) {
        return reactiveMongoTemplate.save(university);
    }

    public Mono<University> getUniversityById(String id) {
        return reactiveMongoTemplate.findById(id, University.class);
    }

    public Flux<University> getAllUniversities(){
        return reactiveMongoTemplate.findAll(University.class);
    }

    public Mono<University> deleteUniversityById(String id) {
        val criteria = Criteria.where(University.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.findAndRemove(query, University.class);
    }

    public Mono<Boolean> checkIfExistUniversityById(String universityId){
        val criteria = Criteria.where(University.ID).is(universityId);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.exists(query,University.class);
    }


}
