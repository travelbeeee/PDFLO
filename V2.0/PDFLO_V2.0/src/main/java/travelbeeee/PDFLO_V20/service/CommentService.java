package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.domain.entity.Comment;
import travelbeeee.PDFLO_V20.domain.form.CommentForm;
import travelbeeee.PDFLO_V20.exception.PDFLOException;

import java.util.List;

public interface CommentService {
    void uploadComment(Long memberId, Long itemId, CommentForm commentForm) throws PDFLOException;
    void modifyComment(Long memberId, Long commentId, CommentForm commentForm) throws PDFLOException;
    void deleteComment(Long memberId, Long commentId) throws PDFLOException;
    List<Comment> findAllByItem(Long itemId);
}
