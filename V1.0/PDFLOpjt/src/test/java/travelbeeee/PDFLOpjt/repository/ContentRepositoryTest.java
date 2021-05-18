package travelbeeee.PDFLOpjt.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.Content;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ContentRepositoryTest {
    @Autowired
    ContentRepository contentRepository;

    @AfterEach
    void afterEach(){
        contentRepository.deleteAll();
    }

    Content getContent(){
        Content content = new Content();
        content.setUserId(1);
        content.setPdfId(1);
        content.setThumbnailId(1);
        content.setTitle("title");
        content.setPrice(100);
        content.setContent("content");
        content.setLocaldate(LocalDate.now());
        return content;
    }
    
    @Test
    public void ContentInsert테스트() throws Exception{
        //given
        Content content = getContent();
        System.out.println(content);
        //when
        int res = contentRepository.insert(content);
        //then
        System.out.println(content.getContentId());
        assertThat(res).isEqualTo(1);
        assertThat(content.getContentId()).isNotEqualTo(0);
    }

    @Test
    public void ContentDelete테스트() throws Exception{
        //given
        Content content = getContent();
        //when
        contentRepository.insert(content);
        int res = contentRepository.delete(content.getContentId());
        Content findContent = contentRepository.selectById(content.getContentId());
        //then
        assertThat(res).isEqualTo(1);
        assertThat(findContent).isNull();
    }

    @Test
    public void ContentUpdate테스트() throws Exception{
        //given
        Content content = getContent();
        //when
        contentRepository.insert(content);
        content.setPrice(1000);
        content.setTitle("newTitle");
        content.setContent("newContent");
        int res = contentRepository.update(content);
        Content findContent = contentRepository.selectById(content.getContentId());
        //then
        assertThat(res).isEqualTo(1);
        assertThat(findContent.getTitle()).isEqualTo("newTitle");
        assertThat(findContent.getContent()).isEqualTo("newContent");
        assertThat(findContent.getPrice()).isEqualTo(1000);
    }

    @Test
    public void Content전체select테스트() throws Exception{
        //given
        Content content = getContent();
        Content content1 = getContent();
        Content content2 =getContent();
        //when
        contentRepository.insert(content);
        contentRepository.insert(content1);
        contentRepository.insert(content2);
        List<Content> res = contentRepository.selectAll();
        //then
        assertThat(res.size()).isEqualTo(3);
    }
}