package com.example.studentmanagement.department.repositories;

import com.example.studentmanagement.department.entities.Department;
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
public class DepartmentRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Department> addDepartment(Department department){
        return reactiveMongoTemplate.save(department);
    }

    public Mono<Department> getDepartmentById(String id){
        return reactiveMongoTemplate.findById(id,Department.class);
    }

    public Flux<Department> getAllDepartments(){
        return reactiveMongoTemplate.findAll(Department.class);
    }

    public Mono<Department> deleteDepartmentById(String id){
        val criteria = Criteria.where(Department.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.findAndRemove(query,Department.class);
    }

    public Mono<Boolean> checkIfExistDepartmentWithId(String id){
        val criteria = Criteria.where(Department.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.exists(query,Department.class);
    }

}
