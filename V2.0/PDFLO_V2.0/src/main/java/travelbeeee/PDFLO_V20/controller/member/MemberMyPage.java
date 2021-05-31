package travelbeeee.PDFLO_V20.controller.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO_V20.domain.dto.OrderDto;
import travelbeeee.PDFLO_V20.domain.dto.PointHistoryDto;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.entity.Order;
import travelbeeee.PDFLO_V20.domain.entity.PointHistory;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.domain.form.ProfileForm;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.service.MemberService;
import travelbeeee.PDFLO_V20.utility.MailSender;
import travelbeeee.PDFLO_V20.utility.PermissionChecker;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberMyPage {

    private final MemberService memberService;

    /**
     * 회원을 삭제하기 전에 비밀번호를 한 번 더 확인하는 페이지로 보낸다.
     */
    @GetMapping("/member/delete")
    public String checkPasswordBeforeDelete(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        return "/member/delete";
    }

    /**
     * 확인한 비밀번호가 맞다면 회원을 삭제한다.
     */
    @PostMapping("/member/delete")
    public String memberDelete(HttpSession httpSession, String password) throws PDFLOException, NoSuchAlgorithmException {
        PermissionChecker.checkPermission(httpSession);

        Long memberId = (Long) httpSession.getAttribute("id");
        memberService.checkPassword(memberId, password);
        memberService.delete(memberId);
        httpSession.invalidate();
        return "redirect:/";
    }

    /**
     * 비밀번호를 수정하기 전에 비밀번호를 한 번 더 확인하는 페이지로 보낸다.
     */
    @GetMapping("/member/modify")
    public String checkPasswordBeforeModify(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        return "/member/modify";
    }

    /**
     * 확인한 비밀번호가 맞다면 비밀번호 수정 페이지로 이동한다.
     */
    @PostMapping("/member/modifyForm")
    public String memberModifyForm(HttpSession httpSession, String password) throws PDFLOException, NoSuchAlgorithmException {
        PermissionChecker.checkPermission(httpSession);
        Long memberId = (Long) httpSession.getAttribute("id");
        memberService.checkPassword(memberId, password);
        return "/member/modifyForm";
    }

    /**
     * 입력한 새로운 비밀번호로 데이터를 수정한다.
     */
    @PostMapping("/member/modify")
    public String memberModify(HttpSession httpSession, String newPassword) throws PDFLOException, NoSuchAlgorithmException {
        PermissionChecker.checkPermission(httpSession);
        Long memberId = (Long) httpSession.getAttribute("id");
        memberService.updatePassword(memberId, newPassword);

        return "redirect:/";
    }

    /**
     * 마이페이지로 이동하기.
     * 회원 이름 / 회원 포인트를 View에 뿌려주자.
     */
    @GetMapping("/member/mypage")
    public String mypage(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        Long memberId = (Long) httpSession.getAttribute("id");
        Member member = memberService.findMember(memberId);
        model.addAttribute("username", member.getUsername());
        model.addAttribute("point", member.getPoint());

        return "/member/mypage";
    }

    /**
     * 포인트충전 페이지로 이동하기
     */
    @GetMapping("/member/charge")
    public String pointChargeForm(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        return "/member/chargePoint";
    }

    /**
     * 포인트충전하기 --> mypage 로 redirect
     */
    @PostMapping("/member/charge")
    public String pointCharge(HttpSession httpSession, Integer point) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        Long memberId = (Long) httpSession.getAttribute("id");

        memberService.usePoint(memberId, point, PointType.CHARGE);

        return "redirect:/member/mypage";
    }

    /**
     * 프로필 등록 페이지로 넘어가기
     */
    @GetMapping("/member/profile")
    public String profileForm(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        model.addAttribute("profileForm", new ProfileForm());
        return "/member/profile";
    }

    /**
     * 파일을 제대로 입력했다면, 프로필을 등록하기.
     */
    @PostMapping("/member/profile")
    public String profileUpload(HttpSession httpSession, ProfileForm profileForm) throws PDFLOException, NoSuchAlgorithmException, IOException {
        PermissionChecker.checkPermission(httpSession);
        if (profileForm.getProfile().isEmpty()) {
            return "/member/profile";
        }

        Long memberId = (Long) httpSession.getAttribute("id");
        memberService.uploadProfile(memberId, profileForm);
        return "redirect:/member/mypage";
    }

    /**
     * 해당 회원의 프로필 파일 삭제하기.
     */
    @PostMapping("/member/deleteProfile")
    public String deleteProfile(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        Long memberId = (Long) httpSession.getAttribute("id");
        memberService.deleteProfile(memberId);

        return "redirect:/member/mypage";
    }

    /**
     * 해당 회원의 주문 내역 가지고오기
     */
    @GetMapping("/member/order")
    public String orderHistory(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        Long memberId = (Long) httpSession.getAttribute("id");
        List<Order> orders = memberService.findOrder(memberId);

        List<OrderDto> orderList = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        model.addAttribute("orderList", orderList);

        return "/member/order";
    }

    /**
     * 해당 회원의 포인트 사용 내역 가져오기
     */
    @GetMapping("/member/point")
    public String pointHistory(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        Long memberId = (Long) httpSession.getAttribute("id");

        List<PointHistory> memberPointHistory = memberService.findMemberPointHistory(memberId);

        List<PointHistoryDto> pointHistoryList = memberPointHistory.stream()
                .map(ph -> new PointHistoryDto(ph))
                .collect(Collectors.toList());

        model.addAttribute("pointHistoryList", pointHistoryList);

        return "/member/pointHistory";
    }
}
