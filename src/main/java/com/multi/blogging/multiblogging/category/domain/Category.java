package com.multi.blogging.multiblogging.category.domain;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import com.multi.blogging.multiblogging.board.domain.Board;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, length = 20)
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
    private List<Category> childrenCategories = new ArrayList<>();

    public Category(String title,Member member){
        this.title=title;
        changeMember(member);
    }

    public void changeMember(Member member){
        this.member = member;
        member.getCategories().add(this);
    }
    public void changeParentCategory(Category parent){
        this.parent=parent;
        parent.getChildrenCategories().add(this);
    }

    protected Category(){}
    @Override
    public String toString() {
        return title;
    }
}
