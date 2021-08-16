package travelbeeee.PDFLO.domain.service;

import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Order;
import travelbeeee.PDFLO.domain.model.entity.OrderItem;

import java.util.List;

public interface OrderService {
    void putOrder(Long memberId, List<Long> itemIds) throws PDFLOException;
    List<OrderItem> findOrderItemWithItemThumbnailByOrder(Long orderId);
}