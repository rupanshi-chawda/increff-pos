package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractVersionPojo implements Serializable {
//todo: add nullable false
    @CreationTimestamp
    @Column
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column
    private ZonedDateTime updatedAt;

    @Version
    @Column
    private Integer version;
}
