package com.example.studentmanagement.professor.repositories;

import com.example.studentmanagement.professor.entities.Professor;
import com.example.studentmanagement.professor.entities.TeachesCourse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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

    public Mono<Professor> addTeachesCourse(String professorId, TeachesCourse teachesCourse) {
        val c1 = Criteria.where(Professor.ID).is(professorId);
        val query = Query.query(c1);

        val update = new Update().addToSet(Professor.TEACHES_COURSES).value(teachesCourse);
        return reactiveMongoTemplate.updateFirst(query, update, Professor.class)
                .then(getProfessorById(professorId));
    }

    public Mono<Boolean> checkIfProfessorTeachCourse(String professorId, String courseId) {
        val c1 = Criteria.where(Professor.ID).is(professorId);
        val c2 = Criteria.where(TeachesCourse.COURSE_ID).is(courseId);
        val c3 = Criteria.where(Professor.TEACHES_COURSES).elemMatch(c2);
        val c = new Criteria().andOperator(c1, c3);
        val query = Query.query(c);

        return reactiveMongoTemplate.exists(query, Professor.class);
    }


}
