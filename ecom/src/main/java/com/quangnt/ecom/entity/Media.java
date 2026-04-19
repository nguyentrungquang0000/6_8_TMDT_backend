package com.quangnt.ecom.entity;

import com.quangnt.common.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String url;

    private String contentType;

    private Long size;

    private String fileKey;

    private Boolean status = false;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = String.valueOf(UUID.randomUUID());
        }
    }
}
