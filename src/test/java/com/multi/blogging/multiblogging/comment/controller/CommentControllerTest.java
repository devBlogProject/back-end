package com.multi.blogging.multiblogging.comment.controller;

import com.jayway.jsonpath.JsonPath;
import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.service.MemberService;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.service.BoardService;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.service.CategoryService;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.dto.request.CommentRequestDto;
import com.multi.blogging.multiblogging.comment.dto.request.CommentUpdateRequestDto;
import com.multi.blogging.multiblogging.comment.repository.CommentRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.multi.blogging.multiblogging.Constant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {
    @Autowired
    CommentController commentController;
    @Autowired
    MemberService memberService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BoardService boardService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    CommentService commentService;
    Member member;
    Category category;
    Board board;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();
        memberSignUpRequestDto.setEmail(TEST_EMAIL);
        memberSignUpRequestDto.setPassword(TEST_PASSWORD);
        memberSignUpRequestDto.setNickName(TEST_NICK);
        member = memberService.signUp(memberSignUpRequestDto);
        category=categoryService.addTopCategory("title");

        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setCategoryId(category.getId());
                boardRequestDto.setContent("content");
                boardRequestDto.setTitle("title");
        board =boardService.writeBoard(boardRequestDto, null);

    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void writeComment() throws Exception {
        CommentRequestDto commentRequestDto =new CommentRequestDto();
        commentRequestDto.setBoardId(board.getId());
        commentRequestDto.setContent("comment");

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.content").value("comment"));
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void updateComment() throws  Exception{
        Comment comment = commentService.writeComment(board.getId(), "comment");

        CommentUpdateRequestDto commentUpdateRequestDto = new CommentUpdateRequestDto();
        commentUpdateRequestDto.setContent("updateComment");
        mockMvc.perform(put("/comment/{id}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("updateComment"));
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void 권한체크() throws Exception{
        CommentRequestDto commentRequestDto =new CommentRequestDto();
        commentRequestDto.setBoardId(board.getId());
        commentRequestDto.setContent("comment");

        var result=mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.content").value("comment"))
                .andReturn();

        String response =result.getResponse().getContentAsString();
        Long commentId= ((Number)JsonPath.read(response,"$.data.id")).longValue();

        setAuthNewUser();
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user = SecurityMockMvcRequestPostProcessors.user("abc@abc.com");
        mockMvc.perform(delete("/comment/{id}", commentId)
                        .with(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

        assertEquals(commentRepository.findAll().size(),1);

        CommentUpdateRequestDto commentUpdateRequestDto = new CommentUpdateRequestDto();
        commentUpdateRequestDto.setContent("updateComment");
        mockMvc.perform(put("/comment/{id}", commentId)
                        .with(user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentUpdateRequestDto)))
                .andExpect(status().isForbidden());
    }

    private void setAuthNewUser(){
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();
        memberSignUpRequestDto.setEmail("abc@abc.com");
        memberSignUpRequestDto.setPassword("1234");
        memberSignUpRequestDto.setNickName("anotherUser");
        memberService.signUp(memberSignUpRequestDto);

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails user = userDetailsService.loadUserByUsername("abc@abc.com");
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,"sampleToken",user.getAuthorities()));
    }

}