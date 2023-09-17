package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.SecurityUtil;
import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.*;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.exception.EmailDuplicateException;
import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.exception.PasswordNotMachingException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.multi.blogging.multiblogging.auth.service.EmailService.AUTH_CODE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;





    @Transactional
    public MemberResponseDto modifyNickName(ModifyNickNameRequestDto dto) {
        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        Optional<Member> member = memberRepository.findOneByMemberEmail(memberEmail);
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        member.get().setNickName(dto.getNickName());
        return MemberResponseDto.of(member.get());
    }

    @Transactional
    public void modifyPassword(ModifyPasswordRequestDto dto) {
        Optional<Member> member = memberRepository.findOneByMemberEmail(SecurityUtil.getCurrentMemberEmail());
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), member.get().getPassword())) {
            throw new PasswordNotMachingException();
        }
        member.get().setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    @Transactional
    public MemberResponseDto getMemberProfile() {
        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        Optional<Member> member = memberRepository.findOneByMemberEmail(memberEmail);
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        return MemberResponseDto.of(member.get());
    }


    @Transactional
    public MemberResponseDto signUp(MemberSignUpRequestDto dto) {
        if (memberRepository.findOneByMemberEmail(dto.getEmail()).isPresent()) {
            log.debug("MemberService.singUp EmailDuplicatedException occur dto.email: {}", dto.getEmail());
            throw new EmailDuplicateException();
        }

        Member member = Member.builder()
                .memberEmail(dto.getEmail())
                .authority(Authority.ROLE_MEMBER)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        memberRepository.save(member);

        return MemberResponseDto.of(member);
    }


}
