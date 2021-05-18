package travelbeeee.PDFLOpjt.repository;

import org.apache.ibatis.annotations.Mapper;
import travelbeeee.PDFLOpjt.domain.Comment;

import java.util.List;

@Mapper
public interface CommentRepository {
    int insert(Comment comment);
    int update(Comment comment);
    Comment selectById(int commentId);
    Comment selectByContentUser(int contentId, int userId);
    List<Comment> selectAllByContent(int contentId);
    int delete(int commentId);
    int deleteAll();
}
