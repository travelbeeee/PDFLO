package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.dto.JoinDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;

public interface MemberService {
    void join(JoinDto joinDto) throws PDFLOException;
}
