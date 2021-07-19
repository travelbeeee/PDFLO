package travelbeeee.PDFLO.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO.domain.exception.ErrorCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Cart;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.Member;
import travelbeeee.PDFLO.domain.repository.CartRepository;
import travelbeeee.PDFLO.domain.repository.ItemRepository;
import travelbeeee.PDFLO.domain.repository.MemberRepository;
import travelbeeee.PDFLO.domain.service.CartService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 1) 회원 존재 확인
     * 2) 아이템 존재 확인
     * 3) 판매자가 자신의 아이템을 장바구니에 등록하는지 확인
     * 4) 장바구니에 이미 담은 상품인지 확인
     * 5) Cart 엔티티 추가
     */
    @Transactional
    @Override
    public void putItemOnCart(Long memberId, Long itemId) throws PDFLOException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()){
            throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);
        }
        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            throw new PDFLOException(ErrorCode.ITEM_NO_EXIST);
        }

        Member member = findMember.get();
        Item item = findItem.get();
        if (item.getMember().getId() == member.getId()) {
            throw new PDFLOException(ErrorCode.MEMBER_IS_SELLER);

        }

        Optional<Cart> findCart = cartRepository.findByMemberAndItem(memberId, itemId);
        if (!findCart.isEmpty()) {
            throw new PDFLOException(ErrorCode.CART_ALREADY_EXIST);
        }

        Cart cart = new Cart(member, item);
        cartRepository.save(cart);
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
            throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);
        }
        Optional<Cart> findCart = cartRepository.findById(cartId);
        if (findMember.isEmpty()) {
            throw new PDFLOException(ErrorCode.CART_NO_EXIST);
        }
        Cart cart = findCart.get();
        if(cart.getMember().getId() != memberId) {
            throw new PDFLOException(ErrorCode.CART_NO_EXIST);
        }
        cartRepository.delete(cart);
    }

    @Override
    public List<Cart> findAllByMemberWithItem(Long memberId) {
        return cartRepository.findAllByMemberWithItemMember(memberId);
    }
}
