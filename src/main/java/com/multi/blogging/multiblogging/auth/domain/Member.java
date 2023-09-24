package com.multi.blogging.multiblogging.auth.domain;

import com.multi.blogging.multiblogging.auth.enums.Authority;
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

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 50)
    private String nickName;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public Member(String email, String password, String nickName, Authority authority) {
        this.email =email;
        this.password=password;
        this.nickName=nickName;
        this.authority=authority;
    }

    public Member() {

    }

    public void updatePassword(PasswordEncoder passwordEncoder,String newPassword){
        setPassword(passwordEncoder.encode(newPassword));
    }
}
