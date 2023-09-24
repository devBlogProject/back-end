package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.SecurityUtil;
import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.*;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.exception.EmailDuplicateException;
import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.exception.NickNameDuplicateException;
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


    @Transactional
    public MemberResponseDto modifyNickName(ModifyNickNameRequestDto dto) {
        String email = SecurityUtil.getCurrentMemberEmail();
        Optional<Member> member = memberRepository.findOneByEmail(email);
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        if (memberRepository.existsByNickName(dto.getNickName())){
            throw new NickNameDuplicateException();
        }
        member.get().setNickName(dto.getNickName());
        return MemberResponseDto.of(member.get());
    }

    @Transactional
    public void modifyPassword(ModifyPasswordRequestDto dto) {
        Optional<Member> member = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail());
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), member.get().getPassword())) {
            throw new PasswordNotMachingException();
        }
        member.get().updatePassword(passwordEncoder,dto.getNewPassword());
    }

    @Transactional
    public MemberResponseDto getMemberProfile() {
        String email = SecurityUtil.getCurrentMemberEmail();
        Optional<Member> member = memberRepository.findOneByEmail(email);
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        return MemberResponseDto.of(member.get());
    }


    @Transactional
    public MemberResponseDto signUp(MemberSignUpRequestDto dto) {
        if (memberRepository.findOneByEmail(dto.getEmail()).isPresent()) {
            log.debug("MemberService.singUp EmailDuplicatedException occur dto.email: {}", dto.getEmail());
            throw new EmailDuplicateException();
        }

        if (memberRepository.existsByNickName(dto.getNickName())){
            log.debug("MemberService.singUp NickNameDuplicatedException occur dto.nickName: {}", dto.getNickName());
            throw new NickNameDuplicateException();
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .nickName(dto.getNickName())
                .authority(Authority.MEMBER)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        memberRepository.save(member);

        return MemberResponseDto.of(member);
    }


    public boolean checkEmailDuplicate(String email){
        return !memberRepository.existsByEmail(email);
    }

    public boolean checkNickNameDuplicate(String nickName){
        return !memberRepository.existsByNickName(nickName);
    }
}
