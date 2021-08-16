package travelbeeee.PDFLO.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.dto.*;
import travelbeeee.PDFLO.domain.model.entity.*;
import travelbeeee.PDFLO.domain.model.enumType.PointType;
import travelbeeee.PDFLO.domain.service.ItemService;
import travelbeeee.PDFLO.domain.service.MemberService;
import travelbeeee.PDFLO.domain.utility.PageMaker;
import travelbeeee.PDFLO.web.form.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final Integer itemSizePerPage = 6;
    private final Integer orderSizePerPage = 6;
    private final Integer pointHistorySizePerPage = 15;
    private final Integer sellHistorySizePerPage = 10;
    private final Integer pageSize = 10;

    private final MemberService memberService;
    private final ItemService itemService;

    /**
     * 회원가입 폼 페이지로 넘겨준다.
     * 로그인 상태로 회원가입을 진행하려고 하면 에러 발생.
     */
    @GetMapping("/signUp")
    public String signUpForm(HttpSession httpSession, Model model) throws PDFLOException {
        model.addAttribute("signUpForm", new SignUpForm());
        return "/member/signUp";
    }

    /**
     * 회원가입 진행
     * 1) 아이디, 비밀번호, 이메일 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     * 2) 메일 인증을 진행하지 않고 넘어온 회원은 다시 회원가입 폼 페이지로 보내기.
     * 3) 다른 메일로 인증하고 우회해서 넘어온 회원은 다시 회원가입 폼 페이지로 보내기.
     * 4) 아이디가 중복되는 회원은 다시 회원가입 폼 페이지로 보내기.
     */
    @PostMapping("/signUp")
    public String signUp(@Valid @ModelAttribute SignUpForm form, BindingResult bindingResult, HttpServletRequest request) throws PDFLOException, NoSuchAlgorithmException {
        log.info("signUp 메소드 실행");
        if (bindingResult.hasErrors()) {
            log.info("bindingResult : {}", bindingResult);
            return "/member/signUp";
        }
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("AUTHORIZATION") == null) { // 메일 인증 안했음!
            log.info("메일 인증을 안했습니다.");
            bindingResult.addError(new FieldError("signUpForm", "email", "메일 인증을 먼저 진행해주세요."));
            log.info("bindingResult : {}", bindingResult);
            return "/member/signUp";
        }
        String authEmail = (String) session.getAttribute("email");
        if (!authEmail.equals(form.getEmail())) {
            log.info("다른 메일로 인증했습니다.");
            bindingResult.addError(new FieldError("signUpForm", "email", "인증한 메일과 입력한 메일이 다릅니다."));
            log.info("bindingResult : {}", bindingResult);
            return "/member/signUp";
        }
        Optional<Member> findMember = memberService.findMemberByUsername(form.getUsername());
        if (findMember.isPresent()) {
            bindingResult.addError(new FieldError("signUpForm", "username", "이미 사용중인 아이디입니다."));
            log.info("bindingResult : {}", bindingResult);
            return "/member/signUp";
        }
        memberService.signUp(form);

        return "redirect:/";
    }

    /**
     * 로그인 폼 페이지로 넘겨준다.
     * 로그인 상태로 로그인을 다시 진행하려고 하면 에러.
     */
    @GetMapping("/login")
    public String loginForm(HttpSession httpSession, Model model) throws PDFLOException {
        model.addAttribute("loginForm", new LoginForm());
        return "/member/login";
    }

    /**
     * 로그인 진행
     * 1) 아이디, 비밀번호 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     * 2) 아이디, 비밀번호가 제대로 입력되었다면 세션에 로그인 상태 유지를 위한 Id 값을 추가
     * 3) 프로필 사진이 있는 회원이라면 사진을 추가해서 기존에 요청을 보냈던 페이지로 넘겨주자.
     */
    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpSession httpSession) throws PDFLOException, NoSuchAlgorithmException, MessagingException {
        if (bindingResult.hasErrors()) {
            return "/member/login";
        }

        Optional<Member> loginMember = memberService.login(loginForm);
        if (loginMember.isEmpty()) {
            bindingResult.reject("LoginFail", null, "아이디 또는 비밀번호가 틀렸습니다.(디폴트메시지)");
            return "/member/login";
        }

        Member member = loginMember.get();
        log.info("로그인 결과 Member : {}", member);
        httpSession.setAttribute("id", member.getId());

        Optional<Profile> findProfile = memberService.findProfileByMember(member.getId());

        if(!findProfile.isEmpty()){
            Profile profile = findProfile.get();
            httpSession.setAttribute("profile", profile.getFileInfo().getLocation() +
                    "resized-" + profile.getFileInfo().getSaltedFileName());
        }
        return "redirect:" + redirectURL;
    }

    /**
     * 로그인 상태라면 세션을 날려서 로그인 상태를 초기화해준다.
     */
    @GetMapping("/logout")
    public String logout(HttpSession httpSession) throws PDFLOException {
        httpSession.invalidate();

        return "redirect:/";
    }

    /**
     * 회원탈퇴 혹은 비밀번호 수정 전에 비밀번호 확인 Form으로 보내기
     */
    @GetMapping("/check")
    public String memberCheckForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());

        return "/member/checkPassword";
    }

    /**
     * 비밀번호 재확인이 완료된 회원은 세션에 "checkPassword" 를 추가해준다.
     */
    @PostMapping("/check")
    public String memberCheck(@Validated @ModelAttribute PasswordForm passwordForm,
                              BindingResult bindingResult, HttpSession httpSession) throws PDFLOException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            return "/member/checkPassword";
        }
        Long memberId = (Long) httpSession.getAttribute("id");

        memberService.checkPassword(memberId, passwordForm.getPassword());
        httpSession.setAttribute("checkPassword", true);

        return "/member/mypage";
    }

    /**
     * 비밀번호 재확인을 하고 온 회원만 비밀번호 수정이 가능하다
     */
    @GetMapping("/modify")
    public String memberModifyForm(HttpSession httpSession, Model model) throws PDFLOException, NoSuchAlgorithmException {
        if(httpSession.getAttribute("checkPassword") == null){
            throw new PDFLOException(ReturnCode.INVALID_ACCESS);
        }

        model.addAttribute("passwordForm", new PasswordForm());
        return "/member/modifyForm";
    }

    /**
     * 입력한 새로운 비밀번호로 데이터를 수정한다.
     */
    @PostMapping("/modify")
    public String memberModify(HttpSession httpSession, @Validated @ModelAttribute PasswordForm passwordForm,
                               BindingResult bindingResult) throws PDFLOException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            return "/member/modifyForm";
        }

        Long memberId = (Long) httpSession.getAttribute("id");
        memberService.updatePassword(memberId, passwordForm.getPassword());

        return "redirect:/";
    }

    /**
     * 마이페이지로 이동하기.
     * 회원 이름 / 회원 포인트를 View에 뿌려주자.
     */
    @GetMapping("/mypage")
    public String mypage(HttpSession httpSession, Model model) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        Member member = memberService.findMember(memberId);
        model.addAttribute("username", member.getUsername());
        model.addAttribute("point", member.getPoint());

        return "/member/mypage";
    }

    /**
     * 포인트충전 페이지로 이동하기
     */
    @GetMapping("/charge")
    public String pointChargeForm(Model model) throws PDFLOException {
        model.addAttribute("pointForm", new PointForm());
        return "/member/chargePoint";
    }

    /**
     * 포인트충전하기 --> mypage 로 redirect
     */
    @PostMapping("/charge")
    public String pointCharge(HttpSession httpSession, @ModelAttribute @Validated PointForm form, BindingResult bindingResult) throws PDFLOException {
        if (bindingResult.hasErrors()) {
            return "/member/charge";
        }
        Long memberId = (Long) httpSession.getAttribute("id");

        memberService.usePoint(memberId, form.getPoint(), PointType.CHARGE);

        return "redirect:/member/mypage";
    }

    /**
     * 프로필 등록 페이지로 넘어가기
     */
    @GetMapping("/profile")
    public String profileForm(Model model) throws PDFLOException {
        model.addAttribute("profileForm", new ProfileForm());
        return "/member/profile";
    }

    /**
     * 파일을 제대로 입력했다면, 프로필을 등록하기.
     */
    @PostMapping("/profile")
    public String profileUpload(HttpSession httpSession,
                                @Validated @ModelAttribute ProfileForm profileForm, BindingResult bindingResult) throws PDFLOException, NoSuchAlgorithmException, IOException {
        if (profileForm.getProfile().isEmpty()) {
            bindingResult.addError(new FieldError("profileForm", "profile", "프로필 사진을 등록해주세요."));
            return "/member/profile";
        }

        Long memberId = (Long) httpSession.getAttribute("id");
        memberService.uploadProfile(memberId, profileForm);
        Profile profile = memberService.findProfileByMember(memberId).orElseThrow(() -> new PDFLOException(ReturnCode.MEMBER_PROFILE_NO_EXIST));
        httpSession.setAttribute("profile",profile.getFileInfo().getLocation() +
                "resized-" + profile.getFileInfo().getSaltedFileName());
        return "redirect:/member/mypage";
    }


    /**
     * 해당 회원의 주문 내역 가지고오기
     */
    @GetMapping("/order/{pageNum}")
    public String orderHistory(HttpSession httpSession, Model model, @PathVariable("pageNum") Integer pageNum) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");

        PageRequest pageRequest = PageRequest.of(pageNum, orderSizePerPage, Sort.by(Sort.Direction.DESC, "id"));

        Page<Order> pageOrder = memberService.findOrderByMember(memberId, pageRequest);

        List<Order> orders = pageOrder.getContent();
        List<OrderDto> orderDtos = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());

        PageMaker.calcPageNum(pageOrder.getTotalPages(), pageSize, pageNum, model);
        model.addAttribute("orders", orderDtos);
        model.addAttribute("curPageNum", pageNum);

        return "/member/order";
    }

    /**
     * 해당 회원의 포인트 사용 내역 가져오기
     */
    @GetMapping("/point/{pageNum}")
    public String pointHistory(HttpSession httpSession, Model model, @PathVariable("pageNum") Integer pageNum) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");

        PageRequest pageRequest = PageRequest.of(pageNum, pointHistorySizePerPage, Sort.by(Sort.Direction.DESC, "id"));

        Page<PointHistory> pageMemberPointHistory = memberService.findPointHistoryBymember(memberId, pageRequest);

        List<PointHistory> memberPointHistory = pageMemberPointHistory.getContent();

        List<PointHistoryDto> pointHistoryList = memberPointHistory.stream()
                .map(ph -> new PointHistoryDto(ph))
                .collect(Collectors.toList());

        PageMaker.calcPageNum(pageMemberPointHistory.getTotalPages(), pageSize, pageNum, model);
        model.addAttribute("pointHistoryList", pointHistoryList);
        model.addAttribute("curPageNum", pageNum);

        return "/member/pointHistory";
    }

    /**
     *  회원이 판매 중인 상품 보기
     */
    @GetMapping("/item/{pageNum}")
    public String memberItem(HttpSession httpSession, Model model, @PathVariable("pageNum") Integer pageNum) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");

        PageRequest pageRequest = PageRequest.of(pageNum, itemSizePerPage, Sort.by(Sort.Direction.DESC, "id"));
        Page<PopularItem> pageItems = itemService.findWithItemAndThumbnailByPagingAndMember(pageRequest, memberId);
        List<ItemDto> itemDtos = pageItems.getContent().stream().map(pi -> new ItemDto(pi))
                .collect(Collectors.toList());

        PageMaker.calcPageNum(pageItems.getTotalPages(), pageSize, pageNum, model);
        model.addAttribute("items", itemDtos);
        model.addAttribute("curPageNum", pageNum);

        return "item/myItem";
    }

    /**
     *  회원이 판매 중인 상품의 판매 내역 보기
     */
    @GetMapping("/item/{itemId}/{pageNum}")
    public String memberSell(HttpSession httpSession, Model model,
                             @PathVariable("itemId") Long itemId, @PathVariable("pageNum") Integer pageNum) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");

        PageRequest pageRequest = PageRequest.of(pageNum, sellHistorySizePerPage, Sort.by(Sort.Direction.DESC, "id"));
        Page<OrderItem> pageOrderItems = memberService.findMemberSellHistory(memberId, itemId, pageRequest);
        List<SellHistoryDto> sellHistoryDtos = pageOrderItems.getContent().stream().map(oi -> new SellHistoryDto(oi))
                .collect(Collectors.toList());

        Item item = itemService.findWithThumbnailById(itemId);
        ItemDetailDto itemDetailDto = new ItemDetailDto(item);

        PageMaker.calcPageNum(pageOrderItems.getTotalPages(), pageSize, pageNum, model);
        model.addAttribute("item", itemDetailDto);
        model.addAttribute("orders", sellHistoryDtos);
        model.addAttribute("curPageNum", pageNum);

        return "item/mySell";
    }

}
