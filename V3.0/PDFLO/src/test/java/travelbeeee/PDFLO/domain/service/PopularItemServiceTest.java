package travelbeeee.PDFLO.domain.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import travelbeeee.PDFLO.domain.model.entity.*;
import travelbeeee.PDFLO.domain.repository.*;

import javax.transaction.Transactional;


@SpringBootTest
class PopularItemServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    PopularItemRepository popularItemRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PopularItemService popularItemService;

    @Test
    @Rollback(value = false)
    @Transactional
    void 테스트데이터생성(){
        Member member = new Member("username", "password", "salt", "email", 0);
        memberRepository.save(member);

        int itemDummySize = 4000;
        for (int i = 0; i < itemDummySize; i++){
            if(i % 2000 == 0){
                System.out.println(i + " 번 째 상품까지 생성 완료");
            }
            Item item = new Item(member, "title" + i, "content" + i, 10000, null, null);
            itemRepository.save(item);

            PopularItem popularItem = new PopularItem(item, 0.0, 0.0, 0, 0);
            popularItemRepository.save(popularItem);

            int orderDummySize = (int)(50 * Math.random());
            int commentDummySize = (int)(50 * Math.random());

            for (int j = 0; j < orderDummySize; j++){
                OrderItem orderItem = new OrderItem(null, item, 10000);
                orderItemRepository.save(orderItem);
            }

            for (int j = 0; j < commentDummySize; j++){
                Comment comment = new Comment(member, item, "comment" + j, 5 * Math.random());
                commentRepository.save(comment);
            }
        }
    }

    @Test
    @Transactional
    @Rollback
    void 인기도갱신테스트(){
        long start = System.currentTimeMillis();
        popularItemService.updatePopularScore();
        long end = System.currentTimeMillis();
        System.out.println("인기도 갱신 알고리즘 시간 측정 결과 : " + (end - start)/1000.0);
    }

}