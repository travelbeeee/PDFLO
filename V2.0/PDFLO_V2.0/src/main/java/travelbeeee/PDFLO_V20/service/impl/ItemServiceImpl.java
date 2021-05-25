package travelbeeee.PDFLO_V20.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLO_V20.domain.FileInformation;
import travelbeeee.PDFLO_V20.domain.entity.*;
import travelbeeee.PDFLO_V20.domain.enumType.FileType;
import travelbeeee.PDFLO_V20.domain.form.ItemForm;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.ItemRepository;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.repository.PdfRepository;
import travelbeeee.PDFLO_V20.repository.ThumbnailRepository;
import travelbeeee.PDFLO_V20.service.ItemService;
import travelbeeee.PDFLO_V20.utility.FileManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final PdfRepository pdfRepository;
    private final ThumbnailRepository thumbnailRepository;
    private final FileManager fileManager;

    @Transactional
    @Override
    public void uploadItem(Long memberId, ItemForm itemDto) throws NoSuchAlgorithmException, IOException, PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        MultipartFile pdfFile = itemDto.getPdfFile();
        MultipartFile thumbnailFile = itemDto.getThumbnailFile();

        FileInformation pdfFileInformation = fileManager.fileUpload(pdfFile, FileType.PDF);
        FileInformation thumbnailFileInformation = fileManager.fileUpload(thumbnailFile, FileType.THUMBNAIL);

        Pdf pdf = new Pdf(pdfFileInformation);
        Thumbnail thumbnail = new Thumbnail(thumbnailFileInformation);

        Member member = findMember.get();

        Item item = new Item(member, itemDto.getTitle(), itemDto.getContent(), itemDto.getPrice(), thumbnail, pdf);

        pdfRepository.save(pdf);
        thumbnailRepository.save(thumbnail);
        itemRepository.save(item);
    }

    /**
     * 1) Item을 찾는다.
     * 2) Item의 주인이 memberId가 맞는지 확인한다.
     * 3) Item의 PDF / Thumbnail 파일을 삭제한다.
     * 4) Item의 PDF / Thumbnail 엔티티를 삭제한다.
     * 5) 새롭게 입력된 PDF / Thumbnail 파일을 저장한다.
     * 6) 새롭게 입력된 PDF / Thumbnail 엔티티를 저장한ㄷ.
     * 7) Item 의 값들을 변경한다.
     */
    @Transactional
    @Override
    public void modifyItem(Long memberId, Long itemId, ItemForm itemDto) throws PDFLOException, NoSuchAlgorithmException, IOException {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) throw new PDFLOException(ErrorCode.ITEM_NO_EXIST);

        Item item = findItem.get();
        if(item.getMember().getId() != memberId) throw new PDFLOException(ErrorCode.MEMBER_NOT_SELLER);

        Pdf pdf = item.getPdf();
        Thumbnail thumbnail = item.getThumbnail();

        fileManager.fileDelete(pdf.getFileInfo().getLocation(), pdf.getFileInfo().getSaltedFileName(), pdf.getFileInfo().getExtension());
        fileManager.fileDelete(thumbnail.getFileInfo().getLocation(), thumbnail.getFileInfo().getSaltedFileName(), thumbnail.getFileInfo().getExtension());

        pdfRepository.delete(pdf);
        thumbnailRepository.delete(thumbnail);

        MultipartFile pdfFile = itemDto.getPdfFile();
        MultipartFile thumbnailFile = itemDto.getThumbnailFile();

        FileInformation newPdfFileInformation = fileManager.fileUpload(pdfFile, FileType.PDF);
        FileInformation newThumbnailFileInformation = fileManager.fileUpload(thumbnailFile, FileType.THUMBNAIL);

        Pdf newPdf = new Pdf(newPdfFileInformation);
        Thumbnail newThumbnail = new Thumbnail(newThumbnailFileInformation);

        pdfRepository.save(newPdf);
        thumbnailRepository.save(newThumbnail);
        item.changeItem(itemDto.getTitle(), item.getContent(), itemDto.getPrice(), newThumbnail, newPdf);
    }

    @Transactional
    @Override
    public void deleteItem(Long memberId, Long itemId) throws PDFLOException {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) throw new PDFLOException(ErrorCode.ITEM_NO_EXIST);

        Item item = findItem.get();
        if(item.getMember().getId() != memberId) throw new PDFLOException(ErrorCode.MEMBER_NOT_SELLER);

        Pdf pdf = item.getPdf();
        Thumbnail thumbnail = item.getThumbnail();

        fileManager.fileDelete(pdf.getFileInfo().getLocation(), pdf.getFileInfo().getSaltedFileName(), pdf.getFileInfo().getExtension());
        fileManager.fileDelete(thumbnail.getFileInfo().getLocation(), thumbnail.getFileInfo().getSaltedFileName(), thumbnail.getFileInfo().getExtension());

        pdfRepository.delete(pdf);
        thumbnailRepository.delete(thumbnail);
        itemRepository.delete(item);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
