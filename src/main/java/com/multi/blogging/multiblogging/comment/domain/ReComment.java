package com.multi.blogging.multiblogging.comment.domain;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ReComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Builder
    public ReComment(Comment parent, Member member, String content){
        this.content = content;
        this.changeParent(parent);
        this.changeMember(member);
    }

    public void changeMember(Member member){
        this.member = member;
        member.getReCommentList().add(this);
    }

    public void changeParent(Comment parent){
        this.parent = parent;
        parent.getChildren().add(this);
    }

    protected ReComment(){};
}
