package com.multi.blogging.multiblogging.board.domain;
import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE board_id = ?")
@Where(clause = "is_deleted = false")
@Getter @Setter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    @Column
    private String thumbnailUrl;

    @Column(nullable = false)
    private int postNumber;

    private int viewCount=0;
    private int likeCount=0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> parentCommentList = new ArrayList<>();

    private boolean isDeleted=false;

    @Builder
    public Board(String title, String content, String thumbnailUrl,int postNumber ) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
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
