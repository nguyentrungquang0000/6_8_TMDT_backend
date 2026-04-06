package com.quangnt.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity {
//    @CreatedDate
//    @Column(name = "created_at", nullable = false, updatable = false)
    protected OffsetDateTime createdAt;
//
//    @CreatedBy
//    @Column(name = "created_by", updatable = false)
//    protected UUID createdBy;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    protected OffsetDateTime updatedAt;
//
//    @LastModifiedBy
//    @Column(name = "updated_by")
//    protected UUID updatedBy;
//
//    @Column(name = "deleted_at")
//    protected OffsetDateTime deletedAt;
//
//    @Column(name = "deleted_by")
//    protected UUID deletedBy;
}

