package com.multi.blogging.multiblogging.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Board {
    @Id
    private Long id;

}
