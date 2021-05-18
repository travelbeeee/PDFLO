package travelbeeee.PDFLOpjt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.Comment;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @AfterEach
    void afterEach(){
        commentRepository.deleteAll();
    }

    Comment getComment(){
        Comment comment = new Comment();
        comment.setContentId(1);
        comment.setUserId(1);
        comment.setLocaldate(LocalDate.now());
        comment.setComments("comment");
        comment.setScore(1);
        return comment;
    }

    @Test
    public void 댓글insert테스트() throws Exception{
        //given
        Comment comment = getComment();
        //when
        int res = commentRepository.insert(comment);
        //then
        assertThat(res).isEqualTo(1);
    }

    @Test
    public void 댓글update테스트() throws Exception{
        //given
        Comment comment = getComment();
        //when
        commentRepository.insert(comment);
        comment.setComments("newComment");
        commentRepository.update(comment);
        Comment findComment = commentRepository.selectById(comment.getCommentId());
        //then
        assertThat(findComment.getComments()).isEqualTo("newComment");
        assertThat(findComment.getCommentId()).isEqualTo(comment.getCommentId());
    }

    @Test
    public void 댓글delete테스트() throws Exception{
        //given
        Comment comment = getComment();
        //when
        commentRepository.insert(comment);
        commentRepository.delete(comment.getCommentId());
        Comment findComment = commentRepository.selectById(comment.getCommentId());
        //then
        assertThat(findComment).isNull();
    }
}