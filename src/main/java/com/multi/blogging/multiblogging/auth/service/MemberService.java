package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.ModifyNickNameRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.ModifyPasswordRequestDto;
import com.multi.blogging.multiblogging.auth.dto.request.UpdateProfileImageRequestDto;
import com.multi.blogging.multiblogging.auth.enums.Authority;
import com.multi.blogging.multiblogging.auth.exception.*;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUploadService imageUploadService;

//    @PostConstruct //테스트 유저 생성
//    private void addTestMember(){
//        Member member = Member.builder()
//                .email(Test_EMAIL)
//                .password(passwordEncoder.encode("1234"))
//                .nickName("test_nick")
//                .authority(Authority.ADMIN)
//                .build();
//        memberRepository.save(member);
//    }


    @Transactional
    public Member modifyNickName(String nickName,String memberEmail) {
        Optional<Member> member = memberRepository.findOneByEmail(memberEmail);
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        if (memberRepository.existsByNickName(nickName)){
            throw new NickNameDuplicateException();
        }
        member.get().setNickName(nickName);
        return member.get();
    }

    @Transactional
    public void modifyPassword(String oldPassword, String newPassword,String memberEmail) {
        Optional<Member> member = memberRepository.findOneByEmail(memberEmail);
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        if (!passwordEncoder.matches(oldPassword, member.get().getPassword())) {
            throw new PasswordNotMachingException();
        }
        member.get().updatePassword(passwordEncoder,newPassword);
    }

    @Transactional(readOnly = true)
    public Member getMemberProfile(String nickname) {
        Optional<Member> member = memberRepository.findByNickName(nickname);
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        return member.get();
    }

    @Transactional
    public Member updateMemberProfileImage(MultipartFile image,String memberEmail){
        Member member = memberRepository.findOneByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
        String imageUrl = imageUploadService.uploadFile(image);
        member.setImageUrl(imageUrl);
        return member;
    }


    @Transactional
    public Member signUp(MemberSignUpRequestDto dto) {
        Member findMember = memberRepository.findOneByEmail(dto.getEmail()).orElse(null);
        if (findMember!=null) {
            log.debug("MemberService.singUp EmailDuplicatedException occur dto.email: {}", dto.getEmail());
            if (findMember.getSocialType()!=null){
                throw new SocialMemberDuplicateException();
            }
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

        return memberRepository.save(member);
    }


    public boolean checkEmailDuplicate(String email){
        return !memberRepository.existsByEmail(email);
    }

    public boolean checkNickNameDuplicate(String nickName){
        return !memberRepository.existsByNickName(nickName);
    }


}

