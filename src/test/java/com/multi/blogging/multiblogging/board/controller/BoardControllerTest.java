package com.multi.blogging.multiblogging.board.controller;

import com.jayway.jsonpath.JsonPath;
import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.auth.service.MemberService;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardImageUploadRequestDto;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.board.service.BoardService;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.dto.request.CategoryRequestDto;
import com.multi.blogging.multiblogging.category.service.CategoryService;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.multi.blogging.multiblogging.Constant.*;
import static com.multi.blogging.multiblogging.board.service.BoardService.DEFAULT_THUMB_URL;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = TEST_EMAIL)
@Transactional
class BoardControllerTest {

    @MockBean
    ImageUploadService imageUploadService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BoardService boardService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    MemberService memberService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    BoardRepository boardRepository;

    ObjectMapper objectMapper = new ObjectMapper();


    Long categoryId;

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


    @BeforeEach
    void setUp() throws Exception {
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto();  //회원가입
        memberSignUpRequestDto.setEmail(TEST_EMAIL);
        memberSignUpRequestDto.setNickName(TEST_NICK);
        memberSignUpRequestDto.setPassword(TEST_PASSWORD);
        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDto)))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(post("/category")  //게시물 작성에 필요한 카테고리
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequestDto("test_category"))))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        categoryId = ((Number) JsonPath.read(response, "$.data.id")).longValue();
    }

    @Test
    void 게시물_수정_삭제_권한체크() throws Exception {
        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle("title");
        boardRequestDto.setContent(String.valueOf(html(body(h1("hello world")))));
        boardRequestDto.setCategoryId(categoryId);
        var result = mockMvc.perform(multipart("/board")
                        .file(new MockMultipartFile("boardRequestDto",
                                "dto",
                                "application/json",
                                objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Long boardId = ((Number) JsonPath.read(response, "$.data.id")).longValue();

        setAuthNewUser();
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user = SecurityMockMvcRequestPostProcessors.user("abc@abc.com");

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/board/{id}", boardId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder.file(new MockMultipartFile("boardRequestDto",
                        "dto",
                        "application/json",
                        objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8))).with(user))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/board/{id}", boardId)
                        .with(user))
                .andExpect(status().isForbidden());

        assertEquals(1, boardRepository.findAll().size());
    }

    @Test
    void 게시물안의_카테고리_제목_중복체크() throws Exception {
        BoardRequestDto boardRequestDto1 = new BoardRequestDto();
        boardRequestDto1.setTitle("title1");
        boardRequestDto1.setContent(String.valueOf(html(body(h1("hello world")))));
        boardRequestDto1.setCategoryId(categoryId);
        var result = mockMvc.perform(multipart("/board")
                        .file(new MockMultipartFile("boardRequestDto",
                                "dto",
                                "application/json",
                                objectMapper.writeValueAsString(boardRequestDto1).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        Long board1Id = ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.data.id")).longValue();


        BoardRequestDto boardRequestDto2 = new BoardRequestDto();
        boardRequestDto2.setTitle("title1");
        boardRequestDto2.setContent(String.valueOf(html(body(h1("hello world")))));
        boardRequestDto2.setCategoryId(categoryId);

        mockMvc.perform(multipart("/board")
                        .file(new MockMultipartFile("boardRequestDto",
                                "dto",
                                "application/json",
                                objectMapper.writeValueAsString(boardRequestDto2).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()).andReturn();


        var builder =
                MockMvcRequestBuilders.multipart("/board/{id}", board1Id);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mockMvc.perform(builder.file(new MockMultipartFile("boardRequestDto",
                                "dto",
                                "application/json",
                                objectMapper.writeValueAsString(boardRequestDto2).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("슬라이스 테스트")
    void getBoards() throws Exception {
        for (int i = 0; i < 10; i++) {
            BoardRequestDto boardRequestDto = new BoardRequestDto();
            boardRequestDto.setTitle("title"+i);
            boardRequestDto.setContent(String.valueOf(html(body(h1("hello world")))));
            boardRequestDto.setCategoryId(categoryId);
            mockMvc.perform(multipart("/board")
                            .file(new MockMultipartFile("boardRequestDto",
                                    "dto",
                                    "application/json",
                                    objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)))
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
            Thread.sleep(100);
        }

        List<LocalDateTime> createdDateList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
            requestParams.add("page", Integer.toString(i));
            requestParams.add("size", Integer.toString(5));
            var result = mockMvc.perform(get("/board/all").params(requestParams))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content", hasSize(5)))
                    .andDo(print()).andReturn();

            String response = result.getResponse().getContentAsString();
            List<String> createdDateStrList = JsonPath.read(response, "$.data.content[*].createdDate");
            createdDateList.addAll(createdDateStrList.stream().map(LocalDateTime::parse).toList());
        }

        for (int i = 1; i < createdDateList.size(); i++) {
            assertTrue(createdDateList.get(i - 1).isAfter(createdDateList.get(i)));
        }
    }

    @Test
    void getBoard() throws Exception {
        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle("title");
        boardRequestDto.setContent(String.valueOf(html(body(h1("hello world")))));
        boardRequestDto.setCategoryId(categoryId);

        var result = mockMvc.perform(multipart("/board")
                        .file(new MockMultipartFile("boardRequestDto",
                                "dto",
                                "application/json",
                                objectMapper.writeValueAsString(boardRequestDto).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        String response = result.getResponse().getContentAsString();

        Long boardId = ((Number) JsonPath.read(response, "$.data.id")).longValue();

        mockMvc.perform(get(String.format("/board/%d", boardId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value(String.valueOf(html(body(h1("hello world"))))))
                .andDo(print());
    }

    @Test
    @DisplayName("콘텐츠에도 이미지 없고 썸네일은 있을 때")
    void writeBoardWithoutThumbNailAndWithImgOfBoard() throws Exception {
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
                .andExpect(jsonPath("$.data.author.email").value(TEST_EMAIL))
                .andDo(print());

    }

    @Test
    @DisplayName("콘텐츠에도 이미지 없고 썸네일 파일도 없을 때")
    void writeBoardWithoutThumbNailAndWithoutImgOfContents() throws Exception {
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
                .andExpect(jsonPath("$.data.author.email").value(TEST_EMAIL))
                .andDo(print());

    }

    @Test
    void writeBoardWithThumbNail() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("thumbnail", "image.jpg", "image/jpg", "<<jpg data>>".getBytes());
        given(imageUploadService.uploadFile(Mockito.any(MultipartFile.class))).willReturn("http://image.file");


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
                .andExpect(jsonPath("$.data.author.email").value(TEST_EMAIL))
                .andDo(print());

    }

    @Test
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