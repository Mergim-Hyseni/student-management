package com.example.studentmanagement.program.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

@Data
public class Passability {
    @NotNull
    @Indexed(unique = true)
    private Integer semester;
    @NotNull private Integer ects;
}
