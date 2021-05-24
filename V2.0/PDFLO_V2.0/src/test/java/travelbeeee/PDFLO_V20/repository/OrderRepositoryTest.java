package travelbeeee.PDFLO_V20.repository;

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

import javax.persistence.EntityManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.text.html.parser.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class OrderRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    EntityManager em;

    @Test
    public void findWithOrderItemById_테스트() throws Exception{
        // given
        Member member1 = new Member("member1", null, null, null, null, 10000);
        Member member2 = new Member("member2", null, null, null, null, 10000);

        Item item1 = new Item(member2, "item1", null, 1000, null, null);
        Item item2 = new Item(member2, "item2", null, 2000, null, null);
        Item item3 = new Item(member2, "item3", null, 3000, null, null);

        memberRepository.save(member1);
        memberRepository.save(member2);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Order order = new Order(member1);
        orderRepository.save(order);

        OrderItem orderItem1 = new OrderItem(order, item1, 10000);
        OrderItem orderItem2 = new OrderItem(order, item2, 20000);
        OrderItem orderItem3 = new OrderItem(order, item3, 30000);
        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);
        orderItemRepository.save(orderItem3);

        em.flush();
        em.clear();

        // when
        List<Order> orders = orderRepository.findWithOrderItemById(order.getId());

        // then
        Assertions.assertThat(orders.size()).isEqualTo(1);
        Assertions.assertThat(orders.get(0).getOrderItems().size()).isEqualTo(3);
    }
}