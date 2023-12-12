package com.multi.blogging.multiblogging.board.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.repository.CommentRepository;
import com.multi.blogging.multiblogging.config.QueryDslTestConfig;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.multi.blogging.multiblogging.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Import(QueryDslTestConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;


    Member testMember;
    Category testCategory;
    @BeforeEach
    void setUp(){
         testMember = Member.builder().email(TEST_EMAIL).password(TEST_PASSWORD).nickName(TEST_NICK).build();
        memberRepository.save(testMember);
        testCategory = new Category("title", testMember);
        categoryRepository.save(testCategory);
    }


    @Test
    void soft_delete(){
        List<Board> boardList = new ArrayList<>();
        for(int i =0; i<10;i++){
            Board board = Board.builder()
                    .title("title")
                    .content("content")
                    .thumbnailUrl("thumbnail")
                    .postNumber(i+1)
                    .build();
            board.changeAuthor(testMember);
            boardList.add(boardRepository.save(board));
        }

        boardRepository.delete(boardList.get(0));
        boardRepository.delete(boardList.get(1));

        assertEquals(8,boardRepository.findAll().size());
        assertFalse(boardRepository.findByIdWithMember(boardList.get(0).getId()).isPresent());
        assertEquals(10,testMember.getBoardList().size());
    }

    @Test
    void findByNicknameAndPostNumberWithMember(){
        Board board = Board.builder()
                .title("title")
                .content("content")
                .thumbnailUrl("thumbnail")
                .postNumber(1)
                .build();
        board.changeAuthor(testMember);
        boardRepository.save(board);

        assertTrue(boardRepository.findByMemberNicknameAndPostNumberWithMember(testMember.getNickName(), 1).isPresent());

    }

    @Test
    void findByIdWithMemberAndComment(){
        Board board = Board.builder()
                .title("title")
                .content("content")
                .thumbnailUrl("thumbnail")
                .postNumber(1)
                .build();

        List<Comment> comments = new ArrayList<>();
        for (int i=0; i<5;i++){
            Comment comment = Comment.builder().board(board).member(testMember).content("comment").build();
            comments.add(comment);
            commentRepository.save(comment);
        }
        board.setParentCommentList(comments);
        boardRepository.save(board);

        assertTrue(boardRepository.findByIdWithMember(board.getId()).isPresent());
    }
    @Test
    void findSliceBy() throws InterruptedException {

        for(int i =0; i<10;i++){
            Board board = Board.builder()
                    .title("title")
                    .content("content")
                    .thumbnailUrl("thumbnail")
                    .postNumber(1)
                    .build();
            boardRepository.save(board);
            Thread.sleep(100);
        }
        PageRequest pageRequest = PageRequest.of(0, 5,Sort.by(Sort.Direction.DESC,"createdDate"));
        Slice<Board> boards=boardRepository.findSliceBy(pageRequest);

        assertEquals(5,boards.getSize());
        assertTrue(boards.getSort().isSorted());

        LocalDateTime maxDate = LocalDateTime.MAX;
        List<Board> boardList = boards.stream().toList();
        for (int i=1;i<boardList.size();i++){
            assertTrue(boardList.get(i-1).getCreatedDate().isAfter(boardList.get(i).getCreatedDate()));
        }

    }

    @Test
    void findSliceWithMember() throws InterruptedException {
        for(int i =0; i<10;i++){
            Board board = Board.builder()
                    .title("title")
                    .content("content")
                    .thumbnailUrl("thumbnail")
                    .postNumber(i+1)
                    .build();
            boardRepository.save(board);
            Thread.sleep(100);
        }
        PageRequest pageRequest = PageRequest.of(0, 5,Sort.by(Sort.Direction.DESC,"createdDate"));
        Slice<Board> boards=boardRepository.findSliceWithMember(pageRequest);

        assertEquals(5,boards.getSize());
        assertTrue(boards.getSort().isSorted());

        List<Board> boardList = boards.stream().toList();
        for (int i=1;i<boardList.size();i++){
            assertTrue(boardList.get(i-1).getCreatedDate().isAfter(boardList.get(i).getCreatedDate()));
        }
    }
}