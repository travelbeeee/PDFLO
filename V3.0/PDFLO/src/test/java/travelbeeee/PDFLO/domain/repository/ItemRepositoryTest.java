package travelbeeee.PDFLO.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLO.domain.model.dto.ItemViewDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void findItemWithCommentStatTest(){
        List<ItemViewDto> itemWithCommentStat = itemRepository.findItemWithCommentStat();
        System.out.println(itemWithCommentStat);
    }
}