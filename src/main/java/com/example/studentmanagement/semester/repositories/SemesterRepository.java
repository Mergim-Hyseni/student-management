package com.example.studentmanagement.semester.repositories;


import com.example.studentmanagement.semester.entities.Semester;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class SemesterRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Semester> addSemester(Semester semester){
        return reactiveMongoTemplate.save(semester);
    }

    public Mono<Semester> getSemesterById(String id){
        return reactiveMongoTemplate.findById(id,Semester.class);
    }

    public Mono<Semester> deleteSemesterById(String id){
        val criteria = Criteria.where(Semester.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.findAndRemove(query,Semester.class);
    }


}
