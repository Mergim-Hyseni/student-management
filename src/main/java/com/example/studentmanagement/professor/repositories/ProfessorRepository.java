package com.example.studentmanagement.professor.repositories;

import com.example.studentmanagement.professor.entities.Professor;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

@Repository
@AllArgsConstructor
public class ProfessorRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Professor> addProfessor(Professor professor){
        return reactiveMongoTemplate.save(professor);
    }

    public Mono<Professor> getProfessorById(String id){
        return reactiveMongoTemplate.findById(id,Professor.class);
    }

    public Mono<Professor> deleteProfessorById(String id){
        val criteria = Criteria.where(Professor.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.findAndRemove(query,Professor.class);
    }




}
