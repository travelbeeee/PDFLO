package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.dto.CartViewDto;
import travelbeeee.PDFLO.domain.model.entity.Cart;
import travelbeeee.PDFLO.domain.service.CartService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    /**
     * 장바구니 목록 가져오기.
     */
    @GetMapping("/cart")
    public String memberCartList(HttpSession httpSession, Model model) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        List<Cart> carts = cartService.findAllByMemberWithItem(memberId);

        List<CartViewDto> cartList = carts.stream().map(c -> new CartViewDto(c))
                .collect(Collectors.toList());

        model.addAttribute("cartList", cartList);

        return "/member/cart";
    }

    /**
     * 장바구니 추가하기
     */
    @PostMapping("/cart/{itemId}")
    public String putItemOnCart(HttpSession httpSession, @PathVariable("itemId") Long itemId) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        cartService.putItemOnCart(memberId, itemId);
        return "redirect:/cart";
    }

    /**
     * 장바구니 삭제하기
     */
    @PostMapping("/cart/delete/{cartId}")
    public String deleteCart(HttpSession httpSession, @PathVariable("cartId") Long cartId) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        cartService.deleteItemOnCart(memberId, cartId);
        return "redirect:/member/cart";
    }
}
