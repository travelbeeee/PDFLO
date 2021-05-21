package travelbeeee.PDFLO_V20.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLO_V20.domain.FileInformation;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.entity.Pdf;
import travelbeeee.PDFLO_V20.domain.entity.Thumbnail;
import travelbeeee.PDFLO_V20.dto.ItemDto;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.ItemRepository;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.repository.PdfRepository;
import travelbeeee.PDFLO_V20.repository.ThumbnailRepository;
import travelbeeee.PDFLO_V20.service.ItemService;
import travelbeeee.PDFLO_V20.utility.FileManager;
import travelbeeee.PDFLO_V20.utility.Sha256Encryption;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
    private final Sha256Encryption sha256Encryption;

    @Value("${file.location}")
    private String fileLocation;

    @Transactional
    @Override
    public void uploadItem(Long memberId, ItemDto itemDto) throws NoSuchAlgorithmException, IOException, PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        Member member = findMember.get();

        MultipartFile pdfFile = itemDto.getPdfFile();
        MultipartFile thumbnailFile = itemDto.getThumbnailFile();

        Pdf pdf = uploadPdf(pdfFile);
        Thumbnail thumbnail = uploadThumbnail(thumbnailFile);

        Item item = new Item(member, itemDto.getTitle(), itemDto.getContent(), itemDto.getPrice(), thumbnail, pdf);
        itemRepository.save(item);
    }

    @Transactional
    private Thumbnail uploadThumbnail(MultipartFile thumbnailFile) throws NoSuchAlgorithmException, IOException {
        String thumbnailExtension = thumbnailFile.getOriginalFilename().substring(thumbnailFile.getOriginalFilename().indexOf("."));

        Thumbnail thumbnail = new Thumbnail(new FileInformation(sha256Encryption.sha256(
                thumbnailFile.getOriginalFilename(), sha256Encryption.makeSalt())
                , "/THUMBNAIL", thumbnailExtension));

        fileManager.fileUpload(thumbnailFile.getInputStream(), fileLocation + "/THUMBNAIL", thumbnail.getFileInfo().getSaltedFileName());
        thumbnailRepository.save(thumbnail);

        return thumbnail;
    }

    @Transactional
    private Pdf uploadPdf(MultipartFile pdfFile) throws NoSuchAlgorithmException, IOException {
        Pdf pdf = new Pdf(new FileInformation(sha256Encryption.sha256(
                pdfFile.getOriginalFilename(), sha256Encryption.makeSalt()),
                "/PDF", ".PDF"));

        fileManager.fileUpload(pdfFile.getInputStream(), fileLocation + "/PDF", pdf.getFileInfo().getSaltedFileName());
        pdfRepository.save(pdf);

        return pdf;
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
    public void modifyItem(Long memberId, Long itemId, ItemDto itemDto) throws PDFLOException, NoSuchAlgorithmException, IOException {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) throw new PDFLOException(ErrorCode.ITEM_NO_EXIST);

        Item item = findItem.get();
        if(item.getMember().getId() != memberId) throw new PDFLOException(ErrorCode.MEMBER_NOT_SELLER);

        Pdf pdf = item.getPdf();
        Thumbnail thumbnail = item.getThumbnail();

        fileManager.fileDelete(fileLocation + pdf.getFileInfo().getLocation(), pdf.getFileInfo().getSaltedFileName());
        fileManager.fileDelete(fileLocation + thumbnail.getFileInfo().getLocation(), thumbnail.getFileInfo().getSaltedFileName());

        pdfRepository.delete(pdf);
        thumbnailRepository.delete(thumbnail);

        MultipartFile pdfFile = itemDto.getPdfFile();
        MultipartFile thumbnailFile = itemDto.getThumbnailFile();

        Pdf newPdf = uploadPdf(pdfFile);
        Thumbnail newThumbnail = uploadThumbnail(thumbnailFile);

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

        fileManager.fileDelete(fileLocation + pdf.getFileInfo().getLocation(), pdf.getFileInfo().getSaltedFileName());
        fileManager.fileDelete(fileLocation + thumbnail.getFileInfo().getLocation(), thumbnail.getFileInfo().getSaltedFileName());

        pdfRepository.delete(pdf);
        thumbnailRepository.delete(thumbnail);
        itemRepository.delete(item);
    }
}
