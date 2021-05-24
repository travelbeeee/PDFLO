package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.exception.PDFLOException;

import java.util.List;

public interface OrderService {
    void putOrder(Long memberId, List<Long> itemIds) throws PDFLOException;
}
