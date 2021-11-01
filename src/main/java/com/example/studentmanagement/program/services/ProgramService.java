package com.example.studentmanagement.program.services;

import com.example.studentmanagement.department.services.DepartmentService;
import com.example.studentmanagement.exceptions.BadRequestException;
import com.example.studentmanagement.faculty.services.FacultyService;
import com.example.studentmanagement.program.entities.Passability;
import com.example.studentmanagement.program.entities.Program;
import com.example.studentmanagement.program.repositories.ProgramRepository;
import com.example.studentmanagement.university.services.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository repository;
    private final DepartmentService departmentService;
    private final FacultyService facultyService;
    private final UniversityService universityService;

    public Mono<Program> addProgram(Program program) {
        return departmentService.checkIfExistDepartmentWithId(program.getDepartmentId())
                .handle((exist, synchronousSink) -> {
                    if (exist) synchronousSink.complete();
                    else synchronousSink.error(new BadRequestException("Department of this program doesn't exist"));
                }).then(facultyService.checkIfExistFacultyById(program.getFacultyId()))
                .handle((exist, synchronousSink) -> {
                    if (exist) synchronousSink.complete();
                    else synchronousSink.error(new BadRequestException("Faculty of this program doesn't exist"));
                }).then(universityService.checkIfExistUniversityById(program.getUniversityId()))
                .handle((exist, synchronousSink) -> {
                    if (exist) synchronousSink.complete();
                    else synchronousSink.error(new BadRequestException("University of this program doesn't exist"));
                }).then(repository.addProgram(program));
    }

    public Mono<Program> getProgramById(String id) {
        return repository.getProgramById(id);
    }

    public Mono<Program> deleteProgramById(String id) {
        return repository.deleteProgramById(id);
    }

    public Mono<Program> addPassability(String programId, Passability passability) {
        return repository.checkIfExistProgramById(programId)
                .handle((exist, synchronousSink) -> {
                    if (exist) synchronousSink.complete();
                    else synchronousSink.error(new BadRequestException("The program does not exist"));
                }).then(repository.addPassability(programId, passability));
    }

    public Mono<Boolean> checkForPassability(String programId, String semesterLevel, int semesterNumber, int studentTotalEcts) {
        return repository.checkForPassability(programId, semesterLevel, semesterNumber, studentTotalEcts);
    }



}
