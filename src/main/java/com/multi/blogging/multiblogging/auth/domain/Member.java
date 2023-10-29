package com.multi.blogging.multiblogging.auth.domain;

import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.enums.SocialType;
import com.multi.blogging.multiblogging.base.domain.BaseEntity;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(name="member_email",nullable = false,unique = true, length = 50)
    private String email; //이메일

    @Column(length = 100)
    private String password;

    @Column(length = 50,unique = true)
    private String nickName;

    private String imageUrl; // 프로필 이미지

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickName, Authority authority,SocialType socialType,String socialId,String imageUrl) {
        this.email =email;
        this.password=password;
        this.nickName=nickName;
        this.authority=authority;
        this.socialType=socialType;
        this.socialId=socialId;
        this.imageUrl=imageUrl;
    }

    protected Member() {

    }

    public void updatePassword(PasswordEncoder passwordEncoder,String newPassword){
        setPassword(passwordEncoder.encode(newPassword));
    }
}
