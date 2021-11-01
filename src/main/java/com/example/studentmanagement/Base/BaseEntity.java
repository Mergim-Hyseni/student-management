package com.example.studentmanagement.Base;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
public class BaseEntity {
    public static final String ID = "_id";
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";

    @Id protected String id;
    @CreatedDate protected Instant createdAt;
    @LastModifiedDate protected Instant updatedAt;
}
