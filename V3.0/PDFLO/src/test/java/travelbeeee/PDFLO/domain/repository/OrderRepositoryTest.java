package travelbeeee.PDFLO.domain.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLO.domain.model.entity.Order;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Test
    void orderTest(){
        Optional<Order> findOrder = orderRepository.findOrderWithMemberOrderItemAndItem(19L, 7L);
        Assertions.assertThat(findOrder.isEmpty()).isFalse();
        Assertions.assertThat(findOrder.get().getId()).isEqualTo(33L);
    }
}