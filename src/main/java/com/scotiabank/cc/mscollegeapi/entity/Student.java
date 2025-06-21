package com.scotiabank.cc.mscollegeapi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("student")
public class Student {
    @Id
    private String id;
    private String name;
    @Column("last_name")
    private String lastName;
    private Short age;
    private String status;
}
