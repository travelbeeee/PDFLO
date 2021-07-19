package travelbeeee.PDFLO.domain.service;

import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Order;

import java.util.List;

public interface OrderService {
    void putOrder(Long memberId, List<Long> itemIds) throws PDFLOException;
    List<Order> findOrderByMember(Long memberId);
}