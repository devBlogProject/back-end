package com.multi.blogging.multiblogging.auth.repository;

import com.multi.blogging.multiblogging.auth.domain.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Ref;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    void findByToken(){
        RefreshToken refreshToken=new RefreshToken("test@test.com","123");
        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> findToken= refreshTokenRepository.findByToken("123");

        assertTrue(findToken.isPresent());
        assertEquals(findToken.get().getToken(),"123");
    }
}