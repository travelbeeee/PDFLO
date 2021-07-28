package travelbeeee.PDFLO.domain.service;

import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.model.entity.Cart;

import java.util.List;

public interface CartService {
    ReturnCode putItemOnCart(Long memberId, Long itemId) throws PDFLOException;
    void deleteItemOnCart(Long memberId, Long cartId) throws PDFLOException;
    List<Cart> findAllByMemberWithItem(Long memberId);
}
