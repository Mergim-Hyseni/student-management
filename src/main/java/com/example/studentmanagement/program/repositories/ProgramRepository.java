package com.example.studentmanagement.program.repositories;

import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.program.entities.Passability;
import com.example.studentmanagement.program.entities.Program;
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
public class ProgramRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Program> addProgram(Program program) {
        return reactiveMongoTemplate.save(program);
    }

    public Mono<Program> getProgramById(String id) {
        return reactiveMongoTemplate.findById(id, Program.class);

    }

    public Mono<Program> deleteProgramById(String id) {
        val criteria = Criteria.where(Program.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.findAndRemove(query, Program.class);
    }

    public Mono<Boolean> checkIfExistProgramById(String id) {
        val criteria = Criteria.where(Program.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.exists(query, Program.class);
    }

    public Mono<Program> addPassability(String programId, Passability passability) {
        val criteria = Criteria.where(Program.ID).is(programId);
        val query = Query.query(criteria);
        val update = new Update().addToSet(Program.PASSABILITIES).value(passability);

        return reactiveMongoTemplate.updateFirst(query, update, Program.class)
                .then(getProgramById(programId));
    }

    public Mono<Boolean> checkForPassability(String programId, String semesterLevel, int semesterNumber, int studentTotalEcts) {
        val criteria1 = Criteria.where(Program.ID).is(programId);
        val criteria2 = Criteria.where(Program.LEVEL).is(semesterLevel);
        val criteria3 = Criteria.where(Program.PASSABILITIES_SEMESTER).is(semesterNumber);
        val criteria4 = Criteria.where(Program.PASSABILITIES_ECTS).lte(studentTotalEcts);

        val criteria = new Criteria().andOperator(criteria1, criteria2, criteria3, criteria4);
        val query = Query.query(criteria);

        return reactiveMongoTemplate.exists(query, Program.class);
    }



}
