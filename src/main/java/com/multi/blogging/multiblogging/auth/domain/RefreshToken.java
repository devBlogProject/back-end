package com.multi.blogging.multiblogging.auth.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 1000 * 60 * 60 * 24 * 14) // 2주
public class RefreshToken {
    @Id
    private String memberEmail;
    private String token;

    public RefreshToken updateToken(String token){
        this.token =token;
        return this;
    }
}
