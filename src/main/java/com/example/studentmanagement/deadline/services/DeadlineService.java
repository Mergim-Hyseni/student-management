package com.example.studentmanagement.deadline.services;

import com.example.studentmanagement.deadline.entities.Deadline;
import com.example.studentmanagement.deadline.repositories.DeadlineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class DeadlineService {
    private final DeadlineRepository repository;

    public Mono<Deadline> addDeadline(Deadline deadline) {
        return repository.addDeadline(deadline);
    }

    public Mono<Deadline> getDeadlineById(String id) {
        return repository.getDeadlineById(id);
    }

    public Mono<Deadline> deleteDeadlineById(String id) {
        return repository.deleteDeadlineById(id);
    }

    public Mono<Boolean> checkIfDeadlineIsAvailable(String deadlineId) {
        return repository.checkIfDeadlineIsAvailable(deadlineId);
    }

}
