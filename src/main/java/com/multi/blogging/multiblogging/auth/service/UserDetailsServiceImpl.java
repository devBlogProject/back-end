package com.multi.blogging.multiblogging.auth.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findOneByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(Member member) {

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().getKey());

        return new org.springframework.security.core.userdetails.User(member.getEmail(),
                member.getPassword(),
                Collections.singleton(grantedAuthority));
    }
}
