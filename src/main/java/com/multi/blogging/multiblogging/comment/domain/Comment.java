package com.multi.blogging.multiblogging.comment.domain;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import com.multi.blogging.multiblogging.board.domain.Board;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "parent")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ReComment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Builder
    public Comment(Board board, Member member, String content, List<ReComment> children) {
        this.board = board;
        this.member = member;
        this.content = content;
        this.children = children;
    }

    protected Comment(){}
}
