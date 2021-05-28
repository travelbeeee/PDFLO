package travelbeeee.PDFLO_V20.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.Cart;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.CartRepository;
import travelbeeee.PDFLO_V20.repository.ItemRepository;
import travelbeeee.PDFLO_V20.repository.MemberRepository;
import travelbeeee.PDFLO_V20.service.CartService;

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
     * 1) 회원을 확인
     * 2) 아이템 확인
     * 3) Cart 엔티티 추가
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
    public List<Cart> findAllByMember(Long memberId) {
        return cartRepository.findAllByMember(memberId);
    }
}
