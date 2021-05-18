package travelbeeee.PDFLOpjt.service;

import travelbeeee.PDFLOpjt.domain.Content;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.utility.ReturnCode;
import travelbeeee.PDFLOpjt.inputform.ContentForm;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ContentService {
    ReturnCode upload(ContentForm contentForm, int userId) throws NoSuchAlgorithmException, IOException; // 글등록하기
    ReturnCode delete(int contentId, int userId) throws PDFLOException;
    ReturnCode modify(int contentId, int userId, ContentForm contentForm) throws PDFLOException, IOException, NoSuchAlgorithmException;
    List<Content> selectAll();
    Content selectById(int contentId);
    byte[] download(int userId, int contentId) throws PDFLOException, IOException;
}
