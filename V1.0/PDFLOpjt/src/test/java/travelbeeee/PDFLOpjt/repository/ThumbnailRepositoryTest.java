package travelbeeee.PDFLOpjt.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.Thumbnail;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ThumbnailRepositoryTest {
    @Autowired
    ThumbnailRepository thumbnailRepository;

    @AfterEach
    void afterEach(){
        thumbnailRepository.deleteAll();
    }

    Thumbnail getThumbnail(){
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setContentId(1);
        thumbnail.setOriginFileName("origin");
        thumbnail.setSaltedFileName("salted");
        thumbnail.setLocation("location");

        return thumbnail;
    }

    @Test
    public void 썸네일insert테스트() throws Exception{
        //given
        Thumbnail thumbnail = getThumbnail();
        //when
        int res = thumbnailRepository.insert(thumbnail);
        //then
        assertThat(res).isEqualTo(1);
    }

    @Test
    public void 썸네일delete테스트() throws Exception{
        //given
        Thumbnail thumbnail = getThumbnail();
        //when
        thumbnailRepository.insert(thumbnail);
        int res = thumbnailRepository.delete(thumbnail.getThumbnailId());
        //then
        assertThat(res).isEqualTo(1);
    }
}