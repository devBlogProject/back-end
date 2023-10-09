package com.multi.blogging.multiblogging.auth;

import com.multi.blogging.multiblogging.auth.domain.Member;

import java.util.List;

public class RandomNickNameProvider {

    static public String createRandomNickNameByMembers(String nickName,List<Member> members){
        int idx=0;

        while (true) {
            int finalIdx = idx;
            if (members.stream().filter(member -> member.getNickName().equals(nickName+Integer.toString(finalIdx))).count()>0){
                idx++;
            }else{
                break;
            }
        }

        return nickName+Integer.toString(idx);
    }
}
