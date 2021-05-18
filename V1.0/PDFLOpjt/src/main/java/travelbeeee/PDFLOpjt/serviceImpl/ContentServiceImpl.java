package travelbeeee.PDFLOpjt.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLOpjt.domain.Content;
import travelbeeee.PDFLOpjt.domain.Order;
import travelbeeee.PDFLOpjt.domain.Pdf;
import travelbeeee.PDFLOpjt.domain.Thumbnail;
import travelbeeee.PDFLOpjt.exception.PDFLOException;
import travelbeeee.PDFLOpjt.repository.OrderRepository;
import travelbeeee.PDFLOpjt.service.OrderService;
import travelbeeee.PDFLOpjt.utility.FileManager;
import travelbeeee.PDFLOpjt.utility.ImageResizer;
import travelbeeee.PDFLOpjt.utility.ReturnCode;
import travelbeeee.PDFLOpjt.inputform.ContentForm;
import travelbeeee.PDFLOpjt.repository.ContentRepository;
import travelbeeee.PDFLOpjt.repository.PdfRepository;
import travelbeeee.PDFLOpjt.repository.ThumbnailRepository;
import travelbeeee.PDFLOpjt.service.ContentService;
import travelbeeee.PDFLOpjt.utility.Sha256Util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final PdfRepository pdfRepository;
    private final ThumbnailRepository thumbnailRepository;
    private final FileManager fileManager;
    private final Sha256Util sha256Util;
    private final ImageResizer imageResizer;
    private final OrderRepository orderRepository;

    @Value("${files-location}")
    private String filesLocation;
    /*
        먼저 입력받은 PDF 파일과 Thumbnail 파일을 저장하고 Repository에 insert 한다.
        그 후, Content에 PDF, Thumbnail ID를 설정해 insert하고
        PDF, Thumbnail 의 contentID를 다시 update해준다.
     */
    @Override
    @Transactional // pdf파일, thumbnail파일, content가 동시에 저장되어야지 하나라도 error가 발생하면 Rollback!!
    public ReturnCode upload(ContentForm contentForm, int userId) throws NoSuchAlgorithmException, IOException {
        Pdf pdf = new Pdf();
        pdf.setOriginFileName(contentForm.getPdfFile().getOriginalFilename());
        pdf.setSaltedFileName(sha256Util.sha256(contentForm.getPdfFile().getOriginalFilename(), sha256Util.makeSalt()) + ".pdf");
        pdf.setLocation("/files/PDF");


        fileManager.fileUpload(contentForm.getPdfFile().getInputStream(), filesLocation + "/PDF", pdf.getSaltedFileName());
        pdfRepository.insert(pdf);

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setOriginFileName(contentForm.getThumbnailFile().getOriginalFilename());
        String thumbnailExtension = thumbnail.getOriginFileName().substring(thumbnail.getOriginFileName().indexOf("."));
        thumbnail.setSaltedFileName(sha256Util.sha256(contentForm.getThumbnailFile().getOriginalFilename(), sha256Util.makeSalt()) + thumbnailExtension);
        thumbnail.setLocation("/files/THUMBNAIL");
        fileManager.fileUpload(contentForm.getThumbnailFile().getInputStream(), filesLocation + "/THUMBNAIl", thumbnail.getSaltedFileName());
        imageResizer.resizeImage(filesLocation + "/THUMBNAIL/" + thumbnail.getSaltedFileName(), 400, 400);
        thumbnailRepository.insert(thumbnail);

        Content content = new Content();
        content.setUserId(userId);
        content.setTitle(contentForm.getTitle());
        content.setContent(contentForm.getContent());
        content.setPrice(contentForm.getPrice());
        content.setLocaldate(LocalDate.now());
        content.setPdfId(pdf.getPdfId());
        content.setThumbnailId(thumbnail.getThumbnailId());
        content.setLocaldate(LocalDate.now());
        contentRepository.insert(content);

        pdf.setContentId(content.getContentId());
        thumbnail.setContentId(content.getContentId());

        pdfRepository.updateContentId(pdf);
        thumbnailRepository.updateContentId(thumbnail);

        return ReturnCode.SUCCESS;
    }

    @Override
    public ReturnCode delete(int contentId, int userId) throws PDFLOException {
        Content content = contentRepository.selectById(contentId);
        if(content.getUserId() != userId) throw new PDFLOException(ReturnCode.USER_NOT_SELLER);

        Thumbnail thumbnail = thumbnailRepository.selectById(content.getThumbnailId());
        fileManager.fileDelete(filesLocation + "/THUMBNAIL", thumbnail.getSaltedFileName());
        fileManager.fileDelete(filesLocation + "/THUMBNAIL", "t-" + thumbnail.getSaltedFileName());
        thumbnailRepository.delete(content.getThumbnailId());

        Pdf pdf = pdfRepository.selectById(content.getPdfId());
        fileManager.fileDelete(filesLocation + "/PDF", pdf.getSaltedFileName());
        pdfRepository.delete(content.getPdfId());

        contentRepository.delete(contentId);

        return ReturnCode.SUCCESS;
    }

    @Override
    public ReturnCode modify(int contentId, int userId, ContentForm contentForm) throws PDFLOException, IOException, NoSuchAlgorithmException {
        // 기존에 있던 pdf파일 / thumbnail 파일 삭제하고 다시 저장하기!
        Content content = contentRepository.selectById(contentId);
        Thumbnail thumbnail = thumbnailRepository.selectById(content.getThumbnailId());
        Pdf pdf = pdfRepository.selectById(content.getPdfId());

        fileManager.fileDelete(filesLocation + "/PDF", pdf.getSaltedFileName());
        fileManager.fileDelete(filesLocation + "/THUMBNAIL", thumbnail.getSaltedFileName());
        fileManager.fileDelete(filesLocation + "/THUMBNAIl", "t-" + thumbnail.getSaltedFileName());
        pdfRepository.delete(pdf.getPdfId());
        thumbnailRepository.delete(thumbnail.getThumbnailId());
        contentRepository.delete(contentId);

        upload(contentForm, userId);

        return ReturnCode.SUCCESS;
    }

    @Override
    public List<Content> selectAll() {
        List<Content> contents = contentRepository.selectAll();

        return contents;
    }

    @Override
    public Content selectById(int contentId) {
        Content content = contentRepository.selectById(contentId);
        return content;
    }

    /**
     * userId 가 contentId의 PDF를 다운로드하려고 한다.
     * -> userId가 contentId를 구매했는지 확인.
     */
    @Override
    public byte[] download(int userId, int contentId) throws PDFLOException, IOException {
        Order order = orderRepository.selectByContentUser(contentId, userId);
        if(order == null) throw new PDFLOException(ReturnCode.DOWNLOAD_NO_PERMISSION);

        Content content = contentRepository.selectById(contentId);
        Pdf pdf = pdfRepository.selectById(content.getPdfId());

        return fileManager.fileDownload(filesLocation + "/PDF", pdf.getSaltedFileName());
    }
}
