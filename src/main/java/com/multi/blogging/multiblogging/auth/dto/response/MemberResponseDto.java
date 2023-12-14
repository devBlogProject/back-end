package com.multi.blogging.multiblogging.auth.dto.response;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.enums.SocialType;
import com.multi.blogging.multiblogging.base.BaseResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class MemberResponseDto extends BaseResponseDto {
    private String email; //이메일

    private String nickName;

    private Authority authority;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String imageUrl;

    private SocialType socialType;

    public static MemberResponseDto of(Member member){
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .nickName(member.getNickName())
                .authority(member.getAuthority())
                .createdDate(member.getCreatedDate())
                .updatedDate(member.getUpdatedDate())
                .imageUrl(member.getImageUrl())
                .createdDate(member.getCreatedDate())
                .updatedDate(member.getUpdatedDate())
                .socialType(member.getSocialType())
                .build();
    }
}
