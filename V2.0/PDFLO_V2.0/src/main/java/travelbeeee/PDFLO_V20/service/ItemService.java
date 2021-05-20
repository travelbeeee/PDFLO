package travelbeeee.PDFLO_V20.service;

import travelbeeee.PDFLO_V20.dto.ItemDto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface ItemService{
    void uploadItem(Long memberId, ItemDto itemUploadDto) throws NoSuchAlgorithmException, IOException;
    void modifyItem(Long memberId, Long itemId, ItemDto itemUploadDto);
    void deleteItem(Long memberId, Long itemId);
}