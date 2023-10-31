package com.multi.blogging.multiblogging.comment.domain;

import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReComment extends BaseEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment parent;
}
