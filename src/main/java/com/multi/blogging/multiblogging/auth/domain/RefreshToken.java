package com.multi.blogging.multiblogging.auth.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter @Setter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 1000 * 60 * 60 * 24 * 14) // 2주
public class RefreshToken {
    @NotBlank
    @Id
    private Long memberId;

    @Indexed
    private String token;
}
