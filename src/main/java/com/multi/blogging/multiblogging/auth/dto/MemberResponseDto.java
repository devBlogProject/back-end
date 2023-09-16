package com.multi.blogging.multiblogging.auth.dto;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class MemberResponseDto {
    private String memberEmail; //이메일

    private String nickName;

    private Authority authority;

    private Timestamp createDate;

    public static MemberResponseDto of(Member member){
        return MemberResponseDto.builder()
                .memberEmail(member.getMemberEmail())
                .nickName(member.getNickName())
                .authority(member.getAuthority())
                .createDate(member.getCreateDate())
                .build();
    }
}
