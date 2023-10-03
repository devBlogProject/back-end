package com.multi.blogging.multiblogging.auth.dto;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class MemberResponseDto {
    private String email; //이메일

    private String nickName;

    private Authority authority;

    private Timestamp createDate;

    private String imageUrl;

    public static MemberResponseDto of(Member member){
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .nickName(member.getNickName())
                .authority(member.getAuthority())
                .createDate(member.getCreateDate())
                .imageUrl(member.getImageUrl())
                .build();
    }
}
