package travelbeeee.PDFLOpjt.service;

import travelbeeee.PDFLOpjt.domain.Comment;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.utility.ReturnCode;
import travelbeeee.PDFLOpjt.inputform.CommentForm;

import java.util.List;

public interface CommentService {
    ReturnCode writing(int uesrId, int contentId, CommentForm commentForm) throws PDFLOException; // 해당 유저가 해당 content에 댓글을 씀!
    ReturnCode delete(int userId, int commentId) throws PDFLOException; // 유저가 해당 댓글을 삭제
    ReturnCode update(int userId, int commentId, CommentForm commentForm); //
    Comment selectByUserContent(int userId, int contentId);
    List<Comment> selectAllByContent(int contentId);
    Comment selectByComment(int commentId);
}
