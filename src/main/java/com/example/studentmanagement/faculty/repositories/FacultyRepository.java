package com.example.studentmanagement.faculty.repositories;

import com.example.studentmanagement.department.entities.Department;
import com.example.studentmanagement.faculty.entities.Faculty;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@AllArgsConstructor
public class FacultyRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Faculty> addFaculty(Faculty faculty){
        return reactiveMongoTemplate.save(faculty);
    }

    public Mono<Faculty> getFacultyById(String id){
        return reactiveMongoTemplate.findById(id,Faculty.class);
    }

    public Flux<Faculty> getAllFaculties(){
        return reactiveMongoTemplate.findAll(Faculty.class);
    }

    public Mono<Faculty> deleteFacultyById(String id){
        val critera = Criteria.where(Faculty.ID).is(id);
        val query = Query.query(critera);

        return reactiveMongoTemplate.findAndRemove(query,Faculty.class);
    }

    public Mono<Boolean> checkIfExistFacultyById(String id){
        val criteria = Criteria.where(Faculty.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.exists(query,Faculty.class);
    }


}
