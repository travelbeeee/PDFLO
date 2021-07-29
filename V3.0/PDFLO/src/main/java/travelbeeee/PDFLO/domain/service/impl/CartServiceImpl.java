package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Cart;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.Member;
import travelbeeee.PDFLO.domain.model.entity.Order;
import travelbeeee.PDFLO.domain.repository.CartRepository;
import travelbeeee.PDFLO.domain.repository.ItemRepository;
import travelbeeee.PDFLO.domain.repository.MemberRepository;
import travelbeeee.PDFLO.domain.repository.OrderRepository;
import travelbeeee.PDFLO.domain.service.CartService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    /**
     * 1) 회원 존재 확인
     * 2) 아이템 존재 확인
     * 3) 판매자가 자신의 아이템을 장바구니에 등록하는지 확인
     * 4) 이미 구매한 상품인지 확인
     * 5) Cart 엔티티 추가
     */
    @Transactional
    @Override
    public ReturnCode putItemOnCart(Long memberId, Long itemId) throws PDFLOException {
        log.info("장바구니 추가하기 CartService - putItemOnCart()");
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()){
            log.info("존재하지 않는 회원입니다. putItemOnCart()");
            return ReturnCode.MEMBER_NO_EXIST;
        }

        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            log.info("없는 상품을 구매하려고합니다. putItemOnCart()");
            return ReturnCode.ITEM_NO_EXIST;
        }

        Member member = findMember.get();
        Item item = findItem.get();
        if (item.getMember().getId() == member.getId()) {
            log.info("자신의 상품을 구매하려고합니다. 에러발생");
            return ReturnCode.MEMBER_IS_SELLER;
        }

        Optional<Order> findOrder = orderRepository.findOrderWithMemberOrderItemAndItem(memberId, itemId);
        if (findOrder.isPresent()) {
            log.info("이미 구매한 상품을 구매하려고 합니다. 에러 발생");
            return ReturnCode.ITEM_ALREADY_BOUGHT;
        }

        Optional<Cart> findCart = cartRepository.findByMemberAndItem(memberId, itemId);
        if (!findCart.isEmpty()) {
            return ReturnCode.SUCCESS;
        }

        Cart cart = new Cart(member, item);
        cartRepository.save(cart);
        return ReturnCode.SUCCESS;
    }

    /**
     * 1) 회원 확인
     * 2) 장바구니 확인
     * 3) 장바구니 삭제
     */
    @Transactional
    @Override
    public void deleteItemOnCart(Long memberId, Long cartId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()){
            throw new PDFLOException(ReturnCode.MEMBER_NO_EXIST);
        }
        Optional<Cart> findCart = cartRepository.findById(cartId);
        if (findMember.isEmpty()) {
            throw new PDFLOException(ReturnCode.CART_NO_EXIST);
        }
        Cart cart = findCart.get();
        if(cart.getMember().getId() != memberId) {
            throw new PDFLOException(ReturnCode.CART_NO_EXIST);
        }
        cartRepository.delete(cart);
    }

    @Override
    public List<Cart> findAllByMemberWithItem(Long memberId) {
        return cartRepository.findAllByMemberWithItemMember(memberId);
    }
}
