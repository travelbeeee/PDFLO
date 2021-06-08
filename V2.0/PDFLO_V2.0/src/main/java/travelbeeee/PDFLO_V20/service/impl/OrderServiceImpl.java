package travelbeeee.PDFLO_V20.service.impl;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.*;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.*;
import travelbeeee.PDFLO_V20.service.OrderService;

import javax.mail.FetchProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;

    /**
     * 아직 할인 쿠폰, 정책등을 정하지 않았으므로 그냥 Item 가격으로!
     * 1) 회원 확인
     * 2) 구매하고싶은 상품 확인 -->  N + 1 문제가 발생하므로 Item을 Member와 같이 가져오자.
     * 3) 기존에 구매한 상품이 없는지 확인
     * 4) 회원이 등록한 상품이 없는지 확인
     * 5) 회원이 상품 구매가 가능한 잔고인지 확인
     * 6) 주문 등록!
     *  - Item마다 Member의 PointHistory 추가
     *  - Item마다 OrderItem 추가
     *  - Item마다 장바구니에 담겨져있었다면 취소하기
     */
    @Transactional
    @Override
    public void putOrder(Long memberId, List<Long> itemIds) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);
        Member member = findMember.get();

        // 2)
        List<Item> items = itemRepository.findSelectedItemWithMember(itemIds);
        if(items.size() != itemIds.size()) throw new PDFLOException(ErrorCode.ITEM_NO_EXIST);

        // 3)
        Optional<OrderItem> findOrderItem = orderItemRepository.findByMemberAndItems(member, items);
        if(!findOrderItem.isEmpty()) throw new PDFLOException(ErrorCode.ITEM_ALREADY_BOUGHT);

        // 4)
        for (Item item : items) {
            if(item.getMember().getId() == memberId) throw new PDFLOException(ErrorCode.MEMBER_IS_SELLER);
        }

        // 5)
        int totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice();
        }
        if(member.getPoint() < totalPrice) throw new PDFLOException(ErrorCode.MEMBER_INSUFFICIENT_BALANCE);

        // 6)
        Order order = new Order(member);
        orderRepository.save(order);

        for (Item item : items) {
            Member seller = item.getMember();
            seller.getPoint(item.getPrice());
            pointHistoryRepository.save(new PointHistory(seller, item.getPrice(), PointType.EARN));

            OrderItem orderItem = new OrderItem(order, item, item.getPrice());
            orderItemRepository.save(orderItem);
            pointHistoryRepository.save(new PointHistory(member, item.getPrice(), PointType.USE));
            Optional<Cart> findCart = cartRepository.findByMemberAndItem(memberId, item.getId());
            if (!findCart.isEmpty()) {
                Cart cart = findCart.get();
                cartRepository.delete(cart);
            }
        }
        member.losePoint(totalPrice);
    }

    @Override
    public List<Order> findOrderByMember(Long memberId) {
        return orderRepository.findAllWithOrderItemByMember(memberId);
    }
}
