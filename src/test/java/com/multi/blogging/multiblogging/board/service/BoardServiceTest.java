package com.multi.blogging.multiblogging.board.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import com.multi.blogging.multiblogging.infra.redisDb.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.multi.blogging.multiblogging.Constant.TEST_EMAIL;
import static com.multi.blogging.multiblogging.Constant.TEST_NICK;
import static com.multi.blogging.multiblogging.board.service.BoardService.DEFAULT_THUMB_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static j2html.TagCreator.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BoardService.class})
@ActiveProfiles("test")
class BoardServiceTest {


    @Autowired
    BoardService boardService;
    @MockBean
    BoardRedisClient boardRedisClient;

    @MockBean
    ImageUploadService imageUploadService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    RedisService redisService;


    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    BoardRepository boardRepository;

    private MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpg", "<<jpg data>>".getBytes());


    @Test
    void 썸네일_없이_콘텐츠에_이미지도_없이_업로드() {
        Member testMember = Member.builder().email(TEST_EMAIL).nickName(TEST_NICK).build();
        Category testCategory = new Category("test", testMember);
        given(imageUploadService.uploadFile(any(MultipartFile.class))).willReturn("http://image.file");
        given(memberRepository.findOneByEmail(anyString())).willReturn(Optional.of(testMember));
        given(categoryRepository.findByIdWithMemberAndBoard(anyLong())).willReturn(Optional.of(testCategory));
        given(boardRepository.save(any())).willAnswer((Answer<Board>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Board) args[0];  // 첫 번째 인자를 반환
        });

        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setCategoryId(1L);
        boardRequestDto.setTitle("test_board");
        boardRequestDto.setContent("<html><body></body></html>");
        Board writedBoard = boardService.writeBoard(boardRequestDto, null, TEST_EMAIL);

        assertEquals(writedBoard.getAuthor(), testMember);
        assertEquals(writedBoard.getCategory(), testCategory);
        assertEquals(writedBoard.getThumbnailUrl(), DEFAULT_THUMB_URL);
    }

    @Test
    void 썸네일_없이_콘텐츠에_이미지_있을때_업로드() {
        Member testMember = Member.builder().email(TEST_EMAIL).nickName(TEST_NICK).build();
        Category testCategory = new Category("test", testMember);
        given(imageUploadService.uploadFile(any(MultipartFile.class))).willReturn("http://image.file");
        given(memberRepository.findOneByEmail(anyString())).willReturn(Optional.of(testMember));
        given(categoryRepository.findByIdWithMemberAndBoard(anyLong())).willReturn(Optional.of(testCategory));
        given(boardRepository.save(any())).willAnswer(new Answer<Board>() {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Board) args[0];  // 첫 번째 인자를 반환
            }
        });

        String content = html(
                body(
                        h1("hello, world"),
                        img().withSrc("test_image_1"),
                        img().withSrc("test_image_2"),
                        img().withSrc("test_image_3")

                )
        ).render();

        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setCategoryId(1L);
        boardRequestDto.setTitle("test_board");
        boardRequestDto.setContent(content);
        Board writedBoard = boardService.writeBoard(boardRequestDto, null, TEST_EMAIL);

        assertEquals(writedBoard.getAuthor(), testMember);
        assertEquals(writedBoard.getCategory(), testCategory);
        assertEquals(writedBoard.getThumbnailUrl(), "test_image_1");
    }

    @Test
    void 썸네일_있을때_업로드() {
        Member testMember = Member.builder().email(TEST_EMAIL).nickName(TEST_NICK).build();
        Category testCategory = new Category("test", testMember);
        given(imageUploadService.uploadFile(any(MultipartFile.class))).willReturn("http://image.file");
        given(memberRepository.findOneByEmail(anyString())).willReturn(Optional.of(testMember));
        given(categoryRepository.findByIdWithMemberAndBoard(anyLong())).willReturn(Optional.of(testCategory));
        given(boardRepository.save(any())).willAnswer(new Answer<Board>() {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Board) args[0];  // 첫 번째 인자를 반환
            }
        });

        String content = html(
                body(
                        h1("hello, world"),
                        img().withSrc("test_image_1"),
                        img().withSrc("test_image_2"),
                        img().withSrc("test_image_3")

                )
        ).render();

        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setCategoryId(1L);
        boardRequestDto.setTitle("test_board");
        boardRequestDto.setContent(content);
        Board writedBoard = boardService.writeBoard(boardRequestDto, imageFile, TEST_EMAIL);

        assertEquals(writedBoard.getAuthor(), testMember);
        assertEquals(writedBoard.getCategory(), testCategory);
        assertEquals(writedBoard.getThumbnailUrl(), "http://image.file");
    }

    @Test
    void 포스트_넘버_체크() {
        Member testMember1 = Member.builder().email(TEST_EMAIL).nickName(TEST_NICK).build();
        Category testCategory1 = new Category("test", testMember1);
        given(categoryRepository.findByIdWithMemberAndBoard(anyLong())).willReturn(Optional.of(testCategory1));
        given(boardRepository.save(any())).willAnswer(new Answer<Board>() {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Board) args[0];  // 첫 번째 인자를 반환
            }
        });

        for (int i = 0; i < 5; i++) {  // 기존 유저 게시물 작성 포스트 넘버 테스트
            BoardRequestDto boardRequestDto = new BoardRequestDto();
            boardRequestDto.setCategoryId(1L);
            boardRequestDto.setTitle("title"+i);
            boardRequestDto.setContent("any content");
            assertEquals(i+1,boardService.writeBoard(boardRequestDto, null, TEST_EMAIL).getPostNumber());
        }

        Member testMember2 = Member.builder().email("another_test@test.com").nickName("another_test_nick").build();
        Category testCategory2 = new Category("test", testMember2);
        given(categoryRepository.findByIdWithMemberAndBoard(anyLong())).willReturn(Optional.of(testCategory2));

        for (int i = 0; i < 5; i++) {  // 기존 유저 게시물 작성 포스트 넘버 테스트
            BoardRequestDto boardRequestDto = new BoardRequestDto();
            boardRequestDto.setCategoryId(1L);
            boardRequestDto.setTitle("title"+i);
            boardRequestDto.setContent("any content");
            assertEquals(i+1,boardService.writeBoard(boardRequestDto, null, "another_test@test.com").getPostNumber());
        }
    }

}