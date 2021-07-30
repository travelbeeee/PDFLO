package travelbeeee.PDFLO.domain.repository;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLO.domain.model.entity.Item;

import javax.persistence.EntityManager;
import java.util.Optional;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void ItemDetailDTO테스트(){
        Optional<Item> findItem = itemRepository.findWithMemberAndPdfAndThumbnailAndCommentAndRecommentById(5L);
        Assertions.assertThat(findItem.isPresent()).isTrue();
        Item item = findItem.get();
        Assertions.assertThat(item.getComments().size()).isEqualTo(1);
    }

}