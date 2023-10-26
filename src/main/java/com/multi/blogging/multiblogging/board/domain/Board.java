package com.multi.blogging.multiblogging.board.domain;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> parentCommentList = new ArrayList<>();

    @Builder
    public Board(String title, String content, Member member, String thumbnailUrl, Category category) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.thumbnailUrl = makeDefaultThumb(thumbnailUrl);
        this.category = category;
    }

    /*
       - 썸네일 기본 작성
   */
    private String makeDefaultThumb(String content) {
        String defaultThumbUrl = "https://cdn.pixabay.com/photo/2020/11/08/13/28/tree-5723734_1280.jpg";

        if (thumbnailUrl == null || thumbnailUrl.equals("")) {
            thumbnailUrl = defaultThumbUrl;
        }
        return thumbnailUrl;
    }

    public void changeMember(Member member){
        this.member=member;
        member.getBoardList().add(this);
    }

    public void changeCategory(Category category){
        this.category = category;
        category.getBoardList().add(this);
    }


    protected Board(){}
}
