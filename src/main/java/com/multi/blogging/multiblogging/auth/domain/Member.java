package com.multi.blogging.multiblogging.auth.domain;

import com.multi.blogging.multiblogging.auth.enums.Authority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String memberEmail; //이메일

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(name="member_role")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @CreationTimestamp
    private Timestamp createDate;

    public Member() {

    }
}
