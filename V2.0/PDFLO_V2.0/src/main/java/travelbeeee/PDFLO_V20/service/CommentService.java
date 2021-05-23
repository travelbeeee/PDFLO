package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.dto.CommentDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;

public interface CommentService {
    void uploadComment(Long memberId, Long itemId, CommentDto commentDto) throws PDFLOException;
    void deleteComment(Long memberId, Long commentId) throws PDFLOException;
}
