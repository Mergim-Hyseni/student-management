package com.example.studentmanagement.student.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.security.PublicKey;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmitedCourse extends BaseEntity {
    public static final String DEADLINE_ID = "idDeadline";
    public static final String COURSE_ID = "idCourse";
    public static final String GRADE = "grade";
    public static final String STATUS = "status";
    public static final String GRADE_PLACED_DATE = "gradePlacedDate";
    public static final String ACCEPTED_STATUS = "ACCEPTED";
    public static final String REFUSED_STATUS = "REFUSED";

    @Field(targetType = FieldType.OBJECT_ID)
    private String idCourse;
    private Integer grade;
    @Field(targetType = FieldType.OBJECT_ID)
    private String idDeadline;
    private String status;
    private Instant gradePlacedDate;



}
