package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.domain.form.ItemForm;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.domain.entity.Item;

import javax.mail.FetchProfile;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ItemService{
    void uploadItem(Long memberId, ItemForm itemForm) throws NoSuchAlgorithmException, IOException, PDFLOException; // 상품 등록
    void modifyItem(Long memberId, Long itemId, ItemForm itemUploadDto) throws PDFLOException, NoSuchAlgorithmException, IOException; // 상품 수정
    void deleteItem(Long memberId, Long itemId) throws PDFLOException; // 상품 삭제
    List<Item> findAllWithMemberAndThumbnail(); // 등록된 상품 전체 조회 fetch join Member, Thumbnial
    Item findWithMemberAndPdfAndThumbnailAndCommentById(Long itemId) throws PDFLOException;
}