package travelbeeee.PDFLO.domain.service;

import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.web.form.CommentForm;

import java.util.List;

public interface CommentService {
    ReturnCode uploadComment(Long memberId, Long itemId, CommentForm commentForm) throws PDFLOException;
    ReturnCode deleteComment(Long memberId, Long commentId) throws PDFLOException;
    List<Comment> findAllByItem(Long itemId);
    Comment findById(Long commentId) throws PDFLOException;
    Comment findByIdAndMember(Long commentId, Long memberId) throws PDFLOException;
    List<Comment> findAllWithItemByMember(Long memberId);
}
