package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.dto.ItemDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface ItemService{
    void uploadItem(Long memberId, ItemDto itemUploadDto) throws NoSuchAlgorithmException, IOException, PDFLOException;
    void modifyItem(Long memberId, Long itemId, ItemDto itemUploadDto) throws PDFLOException, NoSuchAlgorithmException, IOException;
    void deleteItem(Long memberId, Long itemId) throws PDFLOException;
}