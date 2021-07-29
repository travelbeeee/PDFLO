package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
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
        log.info("memberCartList 메소드 실행 : 장바구니 목록 가져오기");
        Long memberId = (Long) httpSession.getAttribute("id");
        List<Cart> carts = cartService.findAllByMemberWithItem(memberId);

        List<CartViewDto> cartList = carts.stream().map(c -> new CartViewDto(c))
                .collect(Collectors.toList());

        model.addAttribute("cartList", cartList);

        return "/member/cart";
    }

    /**
     * 장바구니 추가하기
     * API 통신
     */
    @ResponseBody
    @PostMapping("/cart/{itemId}")
    public ReturnCode putItemOnCart(HttpSession httpSession, @PathVariable("itemId") Long itemId) throws PDFLOException {
        log.info("putItemOnCart 메소드 실행 : 장바구니에 추가하기");
        Long memberId = (Long) httpSession.getAttribute("id");
        return cartService.putItemOnCart(memberId, itemId);
    }

    /**
     * 장바구니 삭제하기
     * API 통신
     */
    @ResponseBody
    @DeleteMapping("/cart/{cartId}")
    public ReturnCode deleteCart(HttpSession httpSession, @PathVariable("cartId") Long cartId) throws PDFLOException {
        log.info("CartController - deleteCart");
        Long memberId = (Long) httpSession.getAttribute("id");
        log.info("memberId : {}, cartId : {}", memberId, cartId);
        return cartService.deleteItemOnCart(memberId, cartId);
    }
}
