package com.example.studentmanagement.deadline.entities;


import com.example.studentmanagement.Base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("Deadline")
@AllArgsConstructor
@Builder
public class Deadline extends BaseEntity {
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String LEVEL = "level";
    public static final String TYPE = "type";


    @NotBlank
    private String name;
    private Instant startDate;
    @Future
    private Instant endDate;
    @Pattern(regexp = "Bachelor|Master|Phd")
    private String level;



}
