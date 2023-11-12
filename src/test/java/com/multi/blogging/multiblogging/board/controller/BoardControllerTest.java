package com.multi.blogging.multiblogging.board.controller;

import com.jayway.jsonpath.JsonPath;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.board.dto.request.BoardImageUploadRequestDto;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.category.dto.request.CategoryRequestDto;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import static com.multi.blogging.multiblogging.Constant.*;
import static com.multi.blogging.multiblogging.board.service.BoardService.DEFAULT_THUMB_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static j2html.TagCreator.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BoardControllerTest {

    @MockBean
    ImageUploadService imageUploadService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("콘텐츠에도 이미지 없고 썸네일은 있을 때")
    @WithMockUser(username = TEST_EMAIL)
    void writeBoardWithoutThumbNailAndWithImgOfBoard() throws Exception {

        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();  //회원가입
        memberSignUpRequestDto.setEmail(TEST_EMAIL);
        memberSignUpRequestDto.setNickName(TEST_NICK);
        memberSignUpRequestDto.setPassword(TEST_PASSWORD);
        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDto)))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(post("/category")  //게시판 작성에 필요한 카테고리
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequestDto("test_category"))))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Long categoryId = ((Number) JsonPath.read(response, "$.data.id")).longValue();


        String content = html(body(h1("hello world"),
                img().withSrc("http://image1.com"),
                img().withSrc("http://image2.com"),
                img().withSrc("http://image3.com")
                )).render();
        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setContent(content);
        boardRequestDto.setCategoryId(categoryId);
        boardRequestDto.setTitle("title");

        mockMvc.perform(multipart("/board")
                        .file(new MockMultipartFile("boardRequestDto",
                                "dto",
                                "application/json",
                                objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.thumbnailUrl").value("http://image1.com"))
                .andExpect(jsonPath("$.data.categoryId").value(categoryId))
                .andExpect(jsonPath("$.data.authorResponseDto.email").value(TEST_EMAIL))
                .andDo(print());

    }

    @Test
    @DisplayName("콘텐츠에도 이미지 없고 썸네일 파일도 없을 때")
    @WithMockUser(username = TEST_EMAIL)
    void writeBoardWithoutThumbNailAndWithoutImgOfContents() throws Exception {

        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();  //회원가입
        memberSignUpRequestDto.setEmail(TEST_EMAIL);
        memberSignUpRequestDto.setNickName(TEST_NICK);
        memberSignUpRequestDto.setPassword(TEST_PASSWORD);
        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDto)))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(post("/category")  //게시판 작성에 필요한 카테고리
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequestDto("test_category"))))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Long categoryId = ((Number) JsonPath.read(response, "$.data.id")).longValue();


        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setContent(html(body("hello world")).render());
        boardRequestDto.setCategoryId(categoryId);
        boardRequestDto.setTitle("title");

        mockMvc.perform(multipart("/board")
                        .file(new MockMultipartFile("boardRequestDto",
                                "dto",
                                "application/json",
                                objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.thumbnailUrl").value(DEFAULT_THUMB_URL))
                .andExpect(jsonPath("$.data.categoryId").value(categoryId))
                .andExpect(jsonPath("$.data.authorResponseDto.email").value(TEST_EMAIL))
                .andDo(print());

    }
    @Test
    @WithMockUser(username = TEST_EMAIL)
    void writeBoardWithThumbNail() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("thumbnail", "image.jpg", "image/jpg", "<<jpg data>>".getBytes());
        given(imageUploadService.uploadFile(Mockito.any(MultipartFile.class))).willReturn("http://image.file");

        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto(); //회원가입
        memberSignUpRequestDto.setEmail(TEST_EMAIL);
        memberSignUpRequestDto.setNickName(TEST_NICK);
        memberSignUpRequestDto.setPassword(TEST_PASSWORD);
        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDto)))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(post("/category",TEST_NICK)  //게시판 작성에 필요한 카테고리
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequestDto("test_category"))))
                .andExpect(status().isCreated())
                .andReturn();


        String response = result.getResponse().getContentAsString();
        Long categoryId = ((Number) JsonPath.read(response, "$.data.id")).longValue();


        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setContent(html(body("hello world")).render());
        boardRequestDto.setCategoryId(categoryId);
        boardRequestDto.setTitle("title");

        mockMvc.perform(multipart("/board")
                        .file(imageFile)
                        .file(new MockMultipartFile("boardRequestDto",
                                "dto",
                                "application/json",
                                objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.thumbnailUrl").value("http://image.file"))
                .andExpect(jsonPath("$.data.categoryId").value(categoryId))
                .andExpect(jsonPath("$.data.authorResponseDto.email").value(TEST_EMAIL))
                .andDo(print());

    }

    @Test
    @WithMockUser
    void uploadImage() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpg", "<<jpg data>>".getBytes());
        given(imageUploadService.uploadFile(Mockito.any(MultipartFile.class))).willReturn("http://image.file");

        BoardImageUploadRequestDto requestDto = new BoardImageUploadRequestDto();
        requestDto.setImage(imageFile);

        mockMvc.perform(multipart("/board/image").file(imageFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value("http://image.file"));
    }
}