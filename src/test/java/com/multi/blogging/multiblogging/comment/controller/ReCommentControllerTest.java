package com.multi.blogging.multiblogging.comment.controller;

import com.jayway.jsonpath.JsonPath;
import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.service.MemberService;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.service.BoardService;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.service.CategoryService;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.dto.request.ReCommentRequestDto;
import com.multi.blogging.multiblogging.comment.service.CommentService;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.multi.blogging.multiblogging.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ReCommentControllerTest {

    @Autowired
    MemberService memberService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BoardService boardService;
    @Autowired
    CommentService commentService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserDetailsService userDetailsService;

    Member member;
    Category category;

    Board board;

    Comment parentComment;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();
        memberSignUpRequestDto.setEmail(TEST_EMAIL);
        memberSignUpRequestDto.setPassword(TEST_PASSWORD);
        memberSignUpRequestDto.setNickName(TEST_NICK);
        member = memberService.signUp(memberSignUpRequestDto);
        category = categoryService.addTopCategory("title");

        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setCategoryId(category.getId());
        boardRequestDto.setContent("content");
        boardRequestDto.setTitle("title");
        board = boardService.writeBoard(boardRequestDto, null);

        parentComment = commentService.writeComment(board.getId(), "parent comment");
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void 대댓글_작성() throws Exception{
        var reCommentRequestDto = new ReCommentRequestDto();
        reCommentRequestDto.setContent("child");
        mockMvc.perform(post("/re-comment/parent/{parent_id}",parentComment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reCommentRequestDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/board/{id}",board.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.parentComments[0].reComments[0].content").value("child"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void 권한_체크() throws Exception {
        var reCommentRequestDto = new ReCommentRequestDto();
        reCommentRequestDto.setContent("child");
        var result = mockMvc.perform(post("/re-comment/parent/{parent_id}", parentComment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reCommentRequestDto))
        ).andExpect(status().isCreated()).andDo(print()).andReturn();
        Long id = ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.data.id")).longValue();

        setAuthNewUser();
        var user = SecurityMockMvcRequestPostProcessors.user("abc@abc.com");

        var updateReCommentRequestDto = new ReCommentRequestDto();
        reCommentRequestDto.setContent("updatedChild");
        mockMvc.perform(patch("/re-comment/{id}", id)
                        .with(user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReCommentRequestDto)))
                .andExpect(status().isForbidden())
                .andDo(print());

        mockMvc.perform(delete("/re-comment/{id}", id).with(user))
                .andExpect(status().isForbidden());
    }

    private void setAuthNewUser() {
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();
        memberSignUpRequestDto.setEmail("abc@abc.com");
        memberSignUpRequestDto.setPassword("1234");
        memberSignUpRequestDto.setNickName("anotherUser");
        memberService.signUp(memberSignUpRequestDto);

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails user = userDetailsService.loadUserByUsername("abc@abc.com");
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, "sampleToken", user.getAuthorities()));
    }
}