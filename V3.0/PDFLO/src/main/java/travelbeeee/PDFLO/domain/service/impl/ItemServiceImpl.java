package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLO.domain.exception.Code;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.FileInformation;
import travelbeeee.PDFLO.domain.model.entity.*;
import travelbeeee.PDFLO.domain.model.enumType.FileType;
import travelbeeee.PDFLO.domain.model.enumType.ItemType;
import travelbeeee.PDFLO.domain.repository.*;
import travelbeeee.PDFLO.domain.service.ItemService;
import travelbeeee.PDFLO.domain.utility.FileManager;
import travelbeeee.PDFLO.web.form.ItemForm;

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
    private final OrderItemRepository orderItemRepository;
    private final FileManager fileManager;

    @Transactional
    @Override
    public void uploadItem(Long memberId, ItemForm itemForm) throws NoSuchAlgorithmException, PDFLOException, IOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(Code.MEMBER_NO_EXIST);

        MultipartFile pdfFile = itemForm.getPdfFile();
        MultipartFile thumbnailFile = itemForm.getThumbnailFile();

        FileInformation pdfFileInformation = fileManager.fileSave(pdfFile, FileType.PDF);
        FileInformation thumbnailFileInformation = fileManager.fileSave(thumbnailFile, FileType.THUMBNAIL);

        Pdf pdf = new Pdf(pdfFileInformation);
        Thumbnail thumbnail = new Thumbnail(thumbnailFileInformation);

        Member member = findMember.get();

        Item item = new Item(member, itemForm.getTitle(), itemForm.getContent(), itemForm.getPrice(), thumbnail, pdf, ItemType.SELL);

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
        if(findItem.isEmpty()) throw new PDFLOException(Code.ITEM_NO_EXIST);

        Item item = findItem.get();
        if(item.getMember().getId() != memberId) throw new PDFLOException(Code.MEMBER_NOT_SELLER);

        Pdf pdf = item.getPdf();
        Thumbnail thumbnail = item.getThumbnail();

        fileManager.fileDelete(pdf.getFileInfo().getLocation(), pdf.getFileInfo().getSaltedFileName());
        fileManager.fileDelete(thumbnail.getFileInfo().getLocation(), thumbnail.getFileInfo().getSaltedFileName());

        pdfRepository.delete(pdf);
        thumbnailRepository.delete(thumbnail);

        MultipartFile pdfFile = itemDto.getPdfFile();
        MultipartFile thumbnailFile = itemDto.getThumbnailFile();

        FileInformation newPdfFileInformation = fileManager.fileSave(pdfFile, FileType.PDF);
        FileInformation newThumbnailFileInformation = fileManager.fileSave(thumbnailFile, FileType.THUMBNAIL);

        Pdf newPdf = new Pdf(newPdfFileInformation);
        Thumbnail newThumbnail = new Thumbnail(newThumbnailFileInformation);

        pdfRepository.save(newPdf);
        thumbnailRepository.save(newThumbnail);
        item.changeItem(itemDto.getTitle(), itemDto.getContent(), itemDto.getPrice(), newThumbnail, newPdf);
    }

    /**
     * 1) 상품 확인
     * 2) 요청한 회원이 등록한 상품인지 확인
     * 3) PDF / Thumbnail 파일 삭제하기 (물리적)
     * 4) PDF / Thumbnail / Item 엔티티 삭제하기
     */
    @Transactional
    @Override
    public void deleteItem(Long memberId, Long itemId) throws PDFLOException {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) throw new PDFLOException(Code.ITEM_NO_EXIST);

        Item item = findItem.get();

        if(item.getMember().getId() != memberId) throw new PDFLOException(Code.MEMBER_NOT_SELLER);

        item.stopSell();
    }

    /**
     * 1) 상품 확인
     * 2) 요청한 회원이 등록한 상품인지 확인
     * 3) SELL 로 변경
     */
    @Transactional
    @Override
    public void reSell(Long memberId, Long itemId) throws PDFLOException {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) throw new PDFLOException(Code.ITEM_NO_EXIST);

        Item item = findItem.get();

        if(item.getMember().getId() != memberId) throw new PDFLOException(Code.MEMBER_NOT_SELLER);

        item.reSell();
    }

    /**
     * 1) 상품 확인
     * 2) 회원 확인
     * 3) 회원이 상품 구매했는지 확인
     * 4) PDF파일 다운로드
     * @return
     */
    @Override
    public byte[] downloadItem(Long memberId, Long itemId) throws PDFLOException, IOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(Code.MEMBER_NO_EXIST);

        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) throw new PDFLOException(Code.ITEM_NO_EXIST);

        Member member = findMember.get();
        Item item = findItem.get();

        Optional<OrderItem> findOrderItem = orderItemRepository.findByMemberAndItem(member, item);
        if (findOrderItem.isEmpty()) {
            throw new PDFLOException(Code.DOWNLOAD_NO_PERMISSION);
        }

        FileInformation fileInfo = itemRepository.findWithPDFById(itemId).get().getPdf().getFileInfo();
        return fileManager.fileDownload(fileInfo.getLocation(), fileInfo.getSaltedFileName());
    }

    @Override
    public List<Item> findSellItemWithMemberAndThumbnail() {
        return itemRepository.findSellItemWithMemberAndThumbnail();
    }

    @Override
    public Item findWithMemberAndPdfAndThumbnailAndCommentById(Long itemId) throws PDFLOException {
        Optional<Item> findItem = itemRepository.findWithMemberAndPdfAndThumbnailById(itemId);
        if(findItem.isEmpty()) {
            throw new PDFLOException(Code.ITEM_NO_EXIST);
        }
        return findItem.get();
    }


}
