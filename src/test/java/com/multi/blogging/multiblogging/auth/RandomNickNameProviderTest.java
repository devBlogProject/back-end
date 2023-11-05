package com.multi.blogging.multiblogging.auth;

import com.multi.blogging.multiblogging.auth.domain.Member;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.multi.blogging.multiblogging.Constant.TEST_NICK;
import static org.junit.jupiter.api.Assertions.*;

class RandomNickNameProviderTest {

    static RandomNickNameProvider randomNickNameProvider;


    @Test()
    void 랜덤닉네임(){
        String testNickName=TEST_NICK;
        List<Member> memberList = new ArrayList<Member>();
        memberList.add(Member.builder().nickName(testNickName + "0").build());
        memberList.add(Member.builder().nickName(testNickName + "1").build());
        memberList.add(Member.builder().nickName(testNickName + "2").build());
        memberList.add(Member.builder().nickName(testNickName + "3").build());
        memberList.add(Member.builder().nickName(testNickName + "4").build());

        String randomNickName = RandomNickNameProvider.createRandomNickNameByMembers(TEST_NICK, memberList);

        System.out.println(randomNickName);

        assertEquals(memberList.stream().filter(member -> member.getNickName().equals(randomNickName)).count(),0);
    }
}