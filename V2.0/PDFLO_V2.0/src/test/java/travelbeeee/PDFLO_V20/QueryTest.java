package travelbeeee.PDFLO_V20;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.entity.Order;
import travelbeeee.PDFLO_V20.domain.entity.OrderItem;
import travelbeeee.PDFLO_V20.repository.ItemRepository;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.repository.OrderItemRepository;
import travelbeeee.PDFLO_V20.repository.OrderRepository;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
public class QueryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EntityManager em;

    /**
     * 어떤 회원이 어떤 상품을 구매했는지 조회 하는 쿼리 테스트
     * --> Join Fetch를 이용하면 쿼리 한 번에 해결 가능하다!
     */
    @Transactional
    @Rollback(value = false)
    @Test
    public void 패치조인테스트() throws Exception{
        // Member1이 Item1,2 의 주인 Member2가 Item3의 주인
        // Member3가 Item1,2,3 을 주문했다고 하자!
        Member member1 = new Member("member1", null, null, null, null, null);
        Member member2 = new Member("member2", null, null, null, null, null);
        Member member3 = new Member("member3", null, null, null, null, null);
        Item item1 = new Item(member1, "item1", null, null, null, null);
        Item item2 = new Item(member1, "item2", null, null, null, null);
        Item item3 = new Item(member2, "item3", null, null, null, null);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Order order = new Order(member3);
        orderRepository.save(order);

        OrderItem orderItem1 = new OrderItem(order, item1, 10000);
        OrderItem orderItem2 = new OrderItem(order, item2, 20000);
        OrderItem orderItem3 = new OrderItem(order, item3, 30000);
        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);
        orderItemRepository.save(orderItem3);

        List<OrderItem> resultList = em.createQuery("select oi from OrderItem oi join fetch oi.order o where o.member.id = :memberId and oi.item.id = :itemId", OrderItem.class)
                .setParameter("memberId", member3.getId())
                .setParameter("itemId", item1.getId())
                .getResultList();

        Assertions.assertThat(resultList.size()).isEqualTo(1);
        Assertions.assertThat(resultList.get(0).getOrderPrice()).isEqualTo(10000);
        Assertions.assertThat(resultList.get(0).getOrder()).isEqualTo(order);
        Assertions.assertThat(resultList.get(0).getItem()).isEqualTo(item1);
        Assertions.assertThat(resultList.get(0).getOrder().getMember()).isEqualTo(member3);
    }
}
