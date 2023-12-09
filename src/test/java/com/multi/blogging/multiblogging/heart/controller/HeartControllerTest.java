package com.multi.blogging.multiblogging.heart.controller;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.service.MemberService;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.board.service.BoardService;
import com.multi.blogging.multiblogging.heart.domain.Heart;
import com.multi.blogging.multiblogging.heart.repository.HeartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.multi.blogging.multiblogging.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class HeartControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    HeartRepository heartRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Member member;
    Board board;

    private void setAuthNewUser(String email,String nickname) {
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();
        memberSignUpRequestDto.setEmail(email);
        memberSignUpRequestDto.setPassword("1234");
        memberSignUpRequestDto.setNickName(nickname);
        memberService.signUp(memberSignUpRequestDto);

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails user = userDetailsService.loadUserByUsername(email);
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, "sampleToken", user.getAuthorities()));
    }

    @BeforeEach
    void setUp(){
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();
        memberSignUpRequestDto.setEmail(TEST_EMAIL);
        memberSignUpRequestDto.setPassword(TEST_PASSWORD);
        memberSignUpRequestDto.setNickName(TEST_NICK);
        member=memberService.signUp(memberSignUpRequestDto);

        board = Board.builder().title("title").postNumber(1).build();
        board.changeAuthor(member);
        board=boardRepository.save(Board.builder().title("title").postNumber(1).build());
    }

    @Test
    void getHearts()throws Exception{
        for (int i=0;i<10;i++){
            setAuthNewUser("abc@abc.com"+i,"nickname"+i);
            SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user = SecurityMockMvcRequestPostProcessors.user("abc@abc.com"+i);
            mockMvc.perform(post("/heart/board/{board_id}",board.getId())
                            .with(user)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/heart/board/{board_id}", board.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void 좋아요_삭제() throws Exception {
        mockMvc.perform(post("/heart/board/{board_id}", board.getId()))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(delete("/heart/board/{board_id}", board.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/heart/board/{board_id}", board.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0))
                .andDo(print());
    }
}