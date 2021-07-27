package travelbeeee.PDFLO.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLO.domain.model.dto.ItemViewDto;

import java.util.List;

@SpringBootTest
class ItemJDBCRepositoryTest {
    @Autowired
    ItemJDBCRepository itemJDBCRepository;

    @Test
    void test(){
        List<ItemViewDto> itemViewDto = itemJDBCRepository.findAllItemViewDtoOrderByPopular();
        for (ItemViewDto viewDto : itemViewDto) {
            System.out.println(viewDto);
        }
    }
}