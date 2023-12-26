package com.multi.blogging.multiblogging.tempboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.multi.blogging.multiblogging.auth.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class TempBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member author;

    public void changeAuthor(Member author) {
        this.author = author;
        author.setTempBoard(this);
    }

    private void setAuthor(Member author){};
}
