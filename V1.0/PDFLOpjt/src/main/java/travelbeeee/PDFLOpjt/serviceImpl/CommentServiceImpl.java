package travelbeeee.PDFLOpjt.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLOpjt.domain.Comment;
import travelbeeee.PDFLOpjt.domain.Content;
import travelbeeee.PDFLOpjt.domain.Order;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.repository.ContentRepository;
import travelbeeee.PDFLOpjt.utility.ReturnCode;
import travelbeeee.PDFLOpjt.inputform.CommentForm;
import travelbeeee.PDFLOpjt.repository.CommentRepository;
import travelbeeee.PDFLOpjt.repository.OrderRepository;
import travelbeeee.PDFLOpjt.service.CommentService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final OrderRepository orderRepository;
    private final ContentRepository contentRepository;
    /*
        user가 해당 content를 구매했는지 확인.
        후기는 한 번만 가능하므로 체크.
     */
    @Override
    public ReturnCode writing(int userId, int contentId, CommentForm commentForm) throws PDFLOException {
        // 글쓴이가 후기를 남기는 경우
        Content content = contentRepository.selectById(contentId);
        if(content.getUserId() == userId) throw new PDFLOException(ReturnCode.COMMENT_NO_PERMISSION_BUYING);
        // 일단 user가 content를 구매했는지 확인!
        Order findOrder = orderRepository.selectByContentUser(contentId, userId);
        if(findOrder == null) throw new PDFLOException(ReturnCode.COMMENT_NO_PERMISSION_BUYING);
        Comment findComment = commentRepository.selectByContentUser(contentId, userId);
        if(findComment != null) throw new PDFLOException(ReturnCode.COMMENT_ALREADY_WRITTEN); // 이미 쓴 적이 있음

        Comment comment = new Comment();
        comment.setContentId(contentId);
        comment.setUserId(userId);
        comment.setComments(commentForm.getComments());
        comment.setScore(commentForm.getScore());
        comment.setLocaldate(LocalDate.now());

        commentRepository.insert(comment);

        return ReturnCode.SUCCESS;
    }

    /*
        user가 comment의 주인인지 확인하고 맞다면 delete
    */
    @Override
    public ReturnCode delete(int userId, int commentId) throws PDFLOException {
        Comment findComment = commentRepository.selectById(commentId);
        if(findComment.getUserId() != userId) throw new PDFLOException(ReturnCode.COMMENT_NO_PERMISSION_WRITER);
        commentRepository.delete(commentId);

        return ReturnCode.SUCCESS;
    }

    /*
        해당 유저가 comment의 주인인지 확인하고 맞다면 update를 진행한다.
    */
    @Override
    public ReturnCode update(int userId, int commentId, CommentForm commentForm) {
        Comment findComment = commentRepository.selectById(commentId);
        if(findComment.getUserId() != userId) return ReturnCode.COMMENT_NO_PERMISSION_WRITER;
        findComment.setComments(commentForm.getComments());
        findComment.setScore(commentForm.getScore());
        commentRepository.update(findComment);
        return ReturnCode.SUCCESS;
    }

    @Override
    public Comment selectByUserContent(int userId, int contentId) {
        Comment comment = commentRepository.selectByContentUser(contentId, userId);
        return comment;
    }

    @Override
    public List<Comment> selectAllByContent(int contentId) {
        List<Comment> comments = commentRepository.selectAllByContent(contentId);
        return comments;
    }

    @Override
    public Comment selectByComment(int commentId) {
        return commentRepository.selectById(commentId);
    }
}
