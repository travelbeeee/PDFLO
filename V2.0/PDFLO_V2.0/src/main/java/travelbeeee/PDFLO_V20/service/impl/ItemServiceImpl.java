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

    @Override
    public void uploadItem(Long memberId, ItemDto itemDto) throws NoSuchAlgorithmException, IOException {
        Member member = memberRepository.findById(memberId).get();

        MultipartFile pdfFile = itemDto.getPdfFile();
        MultipartFile thumbnailFile = itemDto.getThumbnailFile();

        Pdf pdf = uploadPdf(pdfFile);
        Thumbnail thumbnail = uploadThumbnail(thumbnailFile);

        Item item = new Item(member, itemDto.getTitle(), itemDto.getContent(), itemDto.getPrice(), thumbnail, pdf);
        itemRepository.save(item);
    }

    private Thumbnail uploadThumbnail(MultipartFile thumbnailFile) throws NoSuchAlgorithmException, IOException {
        String thumbnailExtension = thumbnailFile.getOriginalFilename().substring(thumbnailFile.getOriginalFilename().indexOf("."));

        Thumbnail thumbnail = new Thumbnail(new FileInformation(sha256Encryption.sha256(
                thumbnailFile.getOriginalFilename(), sha256Encryption.makeSalt())
                , "/thumbnail", thumbnailExtension));

        fileManager.fileUpload(thumbnailFile.getInputStream(), fileLocation + "/thumbanil", thumbnail.getFileInfo().getSaltedFileName());
        thumbnailRepository.save(thumbnail);

        return thumbnail;
    }

    private Pdf uploadPdf(MultipartFile pdfFile) throws NoSuchAlgorithmException, IOException {
        Pdf pdf = new Pdf(new FileInformation(sha256Encryption.sha256(
                pdfFile.getOriginalFilename(), sha256Encryption.makeSalt()),
                "/pdf", "pdf"));

        fileManager.fileUpload(pdfFile.getInputStream(), fileLocation + "/pdf", pdf.getFileInfo().getSaltedFileName());
        pdfRepository.save(pdf);

        return pdf;
    }

    @Override
    public void modifyItem(Long memberId, Long itemId, ItemDto itemUploadDto) {

    }

    @Override
    public void deleteItem(Long memberId, Long itemId) {

    }
}
