package com.multi.blogging.multiblogging.auth.domain;

import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(name="member_email",nullable = false, length = 50)
    private String email; //이메일

    @Column(length = 100)
    private String password;

    @Column(length = 50,unique = true)
    private String nickName;

    private String imageUrl; // 프로필 이미지

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @CreationTimestamp
    private Timestamp createDate;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

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

    public Member() {

    }

    public void updatePassword(PasswordEncoder passwordEncoder,String newPassword){
        setPassword(passwordEncoder.encode(newPassword));
    }
}
