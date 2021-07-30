package travelbeeee.PDFLO.domain.service;

import travelbeeee.PDFLO.domain.exception.ReturnCode;

public interface RecommentService {
    ReturnCode uploadRecomment(Long memberId, Long commentId, String commentStr);
    ReturnCode deleteRecomment(Long memberId, Long recommentId);
}
