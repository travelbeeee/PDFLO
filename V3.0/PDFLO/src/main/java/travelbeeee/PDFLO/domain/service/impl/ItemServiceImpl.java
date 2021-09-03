package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final PopularItemRepository popularItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final FileManager fileManager;

    /**
     * 상품 업로드 메소드
     * 1) 상품을 업로드하려는 회원을 조회한다.
     * 2) 입력된 PDF 파일, 썸네일 파일을 저장소에 물리적 저장을 한다.
     * 3) PDF 엔티티, 썸네일 엔티티를 생성해 DB에 저장한다.
     * 4) ITEM 엔티티를 생성하고, 같은 생명 주기를 가지는 PopularItem엔티티도 생성해서 저장한다.
     */
    @Transactional
    @Override
    public void uploadItem(Long memberId, ItemForm itemForm) throws NoSuchAlgorithmException, PDFLOException, IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));

        MultipartFile pdfFile = itemForm.getPdfFile();
        MultipartFile thumbnailFile = itemForm.getThumbnailFile();

        FileInformation pdfFileInformation = fileManager.fileSave(pdfFile, FileType.PDF);
        FileInformation thumbnailFileInformation = fileManager.fileSave(thumbnailFile, FileType.THUMBNAIL);

        Pdf pdf = new Pdf(pdfFileInformation);
        Thumbnail thumbnail = new Thumbnail(thumbnailFileInformation);

        Item item = new Item(member, itemForm.getTitle(), itemForm.getContent(), itemForm.getPrice(), thumbnail, pdf, ItemType.SELL);
        PopularItem popularItem = new PopularItem(item, 0.0);

        itemRepository.save(item);
        popularItemRepository.save(popularItem);
    }

    /**
     * 1) Item을 찾는다.
     * 2) Item의 주인이 memberId가 맞는지 확인한다.
     * 3) Item의 PDF / Thumbnail 파일을 삭제한다.
     * 4) Item의 PDF / Thumbnail 엔티티를 삭제한다.
     * 5) 새롭게 입력된 PDF / Thumbnail 파일을 저장한다.
     * 6) 새롭게 입력된 PDF / Thumbnail 엔티티를 저장한다.
     * 7) Item 의 값들을 변경한다.
     */
    //TODO : 삭제, 삽입 --> 수정
    @Transactional
    @Override
    public void modifyItem(Long memberId, Long itemId, ItemForm form) throws PDFLOException, NoSuchAlgorithmException, IOException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.ITEM_NO_EXIST));

        if(item.getMember().getId() != memberId) {
            throw new PDFLOException(ReturnCode.MEMBER_NOT_SELLER);
        }

        Pdf pdf = item.getPdf();
        Thumbnail thumbnail = item.getThumbnail();

        fileManager.fileDelete(pdf.getFileInfo().getLocation(), pdf.getFileInfo().getSaltedFileName());
        fileManager.fileDelete(thumbnail.getFileInfo().getLocation(), thumbnail.getFileInfo().getSaltedFileName());

        MultipartFile pdfFile = form.getPdfFile();
        MultipartFile thumbnailFile = form.getThumbnailFile();

        FileInformation newPdfFileInformation = fileManager.fileSave(pdfFile, FileType.PDF);
        FileInformation newThumbnailFileInformation = fileManager.fileSave(thumbnailFile, FileType.THUMBNAIL);

        pdf.changePdf(newPdfFileInformation);
        thumbnail.changeThumbnail(newThumbnailFileInformation);
        item.changeItem(form.getTitle(), form.getContent(), form.getPrice());
    }

    /**
     * 1) 상품 확인
     * 2) 요청한 회원이 등록한 상품인지 확인
     * 3) 상품의 상태를 STOP 으로 바꾼다. ( 추후에 다시 판매를 진행할 수 있음. )
     */
    @Transactional
    @Override
    public void deleteItem(Long memberId, Long itemId) throws PDFLOException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow( () -> new PDFLOException(ReturnCode.ITEM_NO_EXIST));

        if(item.getMember().getId() != memberId) throw new PDFLOException(ReturnCode.MEMBER_NOT_SELLER);

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
        Item item = itemRepository.findById(itemId)
        .orElseThrow( () -> new PDFLOException(ReturnCode.ITEM_NO_EXIST));

        if(item.getMember().getId() != memberId) throw new PDFLOException(ReturnCode.MEMBER_NOT_SELLER);

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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_NO_EXIST));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.ITEM_NO_EXIST));

        orderItemRepository.findByMemberAndItem(member, item)
                .orElseThrow(() -> new PDFLOException(ReturnCode.DOWNLOAD_NO_PERMISSION));

        FileInformation fileInfo = itemRepository.findWithPDFById(itemId).get().getPdf().getFileInfo();
        return fileManager.fileDownload(fileInfo.getLocation(), fileInfo.getSaltedFileName());
    }

    @Override
    public Item findWithMemberAndPdfAndThumbnailAndCommentAndRecommentById(Long itemId) throws PDFLOException {
        return itemRepository.findWithMemberAndPdfAndThumbnailAndCommentAndRecommentById(itemId)
                .orElseThrow(() -> new PDFLOException(ReturnCode.ITEM_NO_EXIST));
    }

    @Override
    public Item findWithThumbnailById(Long itemId) throws PDFLOException {
        return itemRepository.findWithThumbnailById(itemId).orElseThrow(() -> new PDFLOException(ReturnCode.ITEM_NO_EXIST));
    }

    @Override
    public Page<PopularItem> findSellItemsWithItemAndThumbnailByPaging(Pageable pageable) {
        return popularItemRepository.findSellPopularItemWithItemAndThumbnailPaging(pageable);
    }

    @Override
    public Page<PopularItem> findWithItemAndThumbnailByPagingAndMember(Pageable pageable, Long memberId) {
        return popularItemRepository.findPopularItemWithItemAndThumbnailPagingByMember(pageable, memberId);
    }

    @Override
    public boolean checkBuyer(Long memberId, Long itemId) {
        Optional<Order> findOrder = orderRepository.findOrderWithMemberOrderItemAndItem(memberId, itemId);
        return findOrder.isPresent();
    }
}
