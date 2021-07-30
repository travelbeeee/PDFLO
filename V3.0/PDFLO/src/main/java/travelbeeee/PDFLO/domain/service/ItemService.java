package travelbeeee.PDFLO.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.PopularItem;
import travelbeeee.PDFLO.web.form.ItemForm;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ItemService{
    void uploadItem(Long memberId, ItemForm itemForm) throws NoSuchAlgorithmException, IOException, PDFLOException; // 상품 등록
    void modifyItem(Long memberId, Long itemId, ItemForm itemUploadDto) throws PDFLOException, NoSuchAlgorithmException, IOException; // 상품 수정
    void deleteItem(Long memberId, Long itemId) throws PDFLOException; // 상품 판매중지
    void reSell(Long memberId, Long itemId) throws PDFLOException; // 상품 재판매
    byte[] downloadItem(Long memberId, Long itemId) throws PDFLOException, IOException;
    List<Item> findSellItemWithMemberAndThumbnail(); // 등록된 상품 전체 조회 fetch join Member, Thumbnail
    Item findWithMemberAndPdfAndThumbnailAndCommentById(Long itemId) throws PDFLOException;
    Item findWithMemberAndPdfAndThumbnailAndCommentAndRecommentById(Long itemId) throws PDFLOException;
    Page<PopularItem> findWithItemAndThumbnailByPaging(Pageable pageable);
    boolean checkBuyer(Long memberId, Long itemId);
}