package com.example.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Data
@Audited
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Long createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Long lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String  createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String  lastModifiedBy;

}
