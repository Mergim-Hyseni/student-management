package com.example.studentmanagement.professor.services;

import com.example.studentmanagement.professor.entities.Professor;
import com.example.studentmanagement.professor.repositories.ProfessorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProfessorService {
    private final ProfessorRepository repository;

    public Mono<Professor> addProfessor(Professor professor){
        return repository.addProfessor(professor);
    }

    public Mono<Professor> getProfessorById(String id){
        return repository.getProfessorById(id);
    }

    public Mono<Professor> deleteProfessorById(String id){
        return repository.deleteProfessorById(id);
    }

}
