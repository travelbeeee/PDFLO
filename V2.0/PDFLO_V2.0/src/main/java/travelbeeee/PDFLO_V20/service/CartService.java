package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.domain.entity.Cart;
import travelbeeee.PDFLO_V20.exception.PDFLOException;

import java.util.List;

public interface CartService {
    void putItemOnCart(Long memberId, Long itemId) throws PDFLOException;
    void deleteItemOnCart(Long memberId, Long cartId) throws PDFLOException;
    List<Cart> findAllByMemberWithItem(Long memberId);
}
