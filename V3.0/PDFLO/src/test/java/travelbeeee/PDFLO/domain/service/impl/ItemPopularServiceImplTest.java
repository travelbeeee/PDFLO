package travelbeeee.PDFLO.domain.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import travelbeeee.PDFLO.domain.service.PopularItemService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemPopularServiceImplTest {

    @Autowired
    PopularItemService popularItemService;

    @Rollback(value = false)
    @Transactional
    @Test
    void 인기도갱신(){
        popularItemService.updatePopularScore();
    }
}