package travelbeeee.PDFLO_V20.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.enumType.MemberType;
import travelbeeee.PDFLO_V20.domain.form.ItemForm;
import travelbeeee.PDFLO_V20.repository.ItemRepository;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.service.ItemService;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ItemServiceImplTest {

    @Autowired
    ItemService itemService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void Item_업로드_테스트() throws Exception{
        //given
        FileInputStream thumbnailFileInputStream = new FileInputStream(new File("../TESTFILES/thumbnail1.JPG"));
        FileInputStream pdfFileInputStream = new FileInputStream(new File("../TESTFILES/pdf1.PDF"));

        MockMultipartFile thumbnailFile = new MockMultipartFile("testFile1", "testFile1.JPG", "JPG", thumbnailFileInputStream);
        MockMultipartFile pdfFile = new MockMultipartFile("testFile2", "testFile2.PDF", "PDF", pdfFileInputStream);

        ItemForm itemDto = new ItemForm("title", "content", 10000, thumbnailFile, pdfFile);

        Member member = new Member("member1", "password1", "salt1", "email1", MemberType.UNAUTHORIZATION, 0);
        memberRepository.save(member);

        itemService.uploadItem(member.getId(), itemDto);
    }

    @Test
    public void Item_삭제_테스트() throws Exception{
        //given
        FileInputStream thumbnailFileInputStream = new FileInputStream(new File("../TESTFILES/thumbnail1.JPG"));
        FileInputStream pdfFileInputStream = new FileInputStream(new File("../TESTFILES/pdf1.PDF"));

        MockMultipartFile thumbnailFile = new MockMultipartFile("testFile1", "testFile1" + "." + "JPG", "JPG", thumbnailFileInputStream);
        MockMultipartFile pdfFile = new MockMultipartFile("testFile2", "testFile2" + "." + "PDF", "PDF", pdfFileInputStream);

        ItemForm itemDto = new ItemForm("title", "content", 10000, thumbnailFile, pdfFile);

        Member member = new Member("member1", "password1", "salt1", "email1", MemberType.UNAUTHORIZATION, 0);
        memberRepository.save(member);

        itemService.uploadItem(member.getId(), itemDto);

        List<Item> items = itemRepository.findByMember(member.getId());
        Item item = items.get(0);

        itemService.deleteItem(member.getId(), item.getId());
    }

    @Test
    public void Item_수정_테스트() throws Exception{
        //given
        FileInputStream thumbnailFileInputStream = new FileInputStream(new File("../TESTFILES/thumbnail1.JPG"));
        FileInputStream pdfFileInputStream = new FileInputStream(new File("../TESTFILES/pdf1.PDF"));

        MockMultipartFile thumbnailFile = new MockMultipartFile("testFile1", "testFile1" + "." + "JPG", "JPG", thumbnailFileInputStream);
        MockMultipartFile pdfFile = new MockMultipartFile("testFile2", "testFile2" + "." + "PDF", "PDF", pdfFileInputStream);

        ItemForm itemDto = new ItemForm("title", "content", 10000, thumbnailFile, pdfFile);

        Member member = new Member("member1", "password1", "salt1", "email1", MemberType.UNAUTHORIZATION, 0);
        memberRepository.save(member);

        itemService.uploadItem(member.getId(), itemDto);

        List<Item> items = itemRepository.findByMember(member.getId());
        Item item = items.get(0);

        FileInputStream newThumbnailFileInputStream = new FileInputStream(new File("../TESTFILES/thumbnail2.JPG"));
        FileInputStream newPdfFileInputStream = new FileInputStream(new File("../TESTFILES/pdf2.PDF"));

        MockMultipartFile newThumbnailFile = new MockMultipartFile("testFile3", "testFile3" + "." + "JPG", "JPG", newThumbnailFileInputStream);
        MockMultipartFile newPdfFile = new MockMultipartFile("testFile4", "testFile4" + "." + "PDF", "PDF", newPdfFileInputStream);

        ItemForm newItemDto = new ItemForm("newTitle", "newContent", 15000, newThumbnailFile, newPdfFile);

        itemService.modifyItem(member.getId(), item.getId(), newItemDto);
    }
}