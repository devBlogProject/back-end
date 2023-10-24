package com.multi.blogging.multiblogging.category.domain;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.board.domain.Board;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "category")
    private List<Board> boardList = new ArrayList<>();

    // 셀프조인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    public Category(String title,Category parent){
        this.title=title;
        this.parent=parent;
    }

    protected Category(){}
    @Override
    public String toString() {
        return title;
    }
}
