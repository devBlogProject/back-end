package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.MemberSignUpResponseDto;
import com.multi.blogging.multiblogging.auth.dto.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberSignUpResponseDto signUp(MemberSignUpRequestDto dto){
        if (memberRepository.findOneByMemberEmail(dto.getEmail()).isPresent()) {
            log.debug("MemberService.singUp EmailDuplicatedException occur dto.email: {}",dto.getEmail());
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member member = Member.builder()
                .memberEmail(dto.getEmail())
                .authority(Authority.ROLE_MEMBER)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        if (dto.getNickName()!=null){
            member.setNickName(dto.getNickName());
        }

        memberRepository.save(member);

        return MemberSignUpResponseDto.of(member);
    }

}
