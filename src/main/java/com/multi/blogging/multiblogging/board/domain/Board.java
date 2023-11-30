package com.multi.blogging.multiblogging.board.domain;
import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition="TEXT")
    private String content;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private int postNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> parentCommentList = new ArrayList<>();

    @Builder
    public Board(String title, String content, Member author, String thumbnailUrl, Category category,int postNumber ) {
        this.title = title;
        this.content = content;
        this.changeAuthor(author);
        this.thumbnailUrl = thumbnailUrl;
        this.changeCategory(category);
        this.postNumber = postNumber;
    }

    /*
       - 썸네일 기본 작성
   */


    public void changeAuthor(Member author){
        this.author =author;
        author.getBoardList().add(this);
    }

    public void changeCategory(Category category){
        this.category = category;
        category.getBoards().add(this);
    }


    protected Board(){}
}
