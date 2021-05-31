package travelbeeee.PDFLO_V20.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO_V20.domain.dto.CartViewDto;
import travelbeeee.PDFLO_V20.domain.dto.ItemViewDto;
import travelbeeee.PDFLO_V20.domain.entity.Cart;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.entity.Profile;
import travelbeeee.PDFLO_V20.domain.enumType.MemberType;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;
import travelbeeee.PDFLO_V20.domain.form.LoginForm;
import travelbeeee.PDFLO_V20.domain.form.ProfileForm;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.service.CartService;
import travelbeeee.PDFLO_V20.service.MemberService;
import travelbeeee.PDFLO_V20.utility.MailSender;
import travelbeeee.PDFLO_V20.utility.PermissionChecker;
import travelbeeee.PDFLO_V20.domain.form.SignUpForm;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MailSender mailSender;
    /**
     * 회원가입 폼 페이지로 넘겨준다.
     * 로그인 상태로 회원가입을 진행하려고 하면 에러 발생.
     */
    @GetMapping("/member/signUp")
    public String signUpForm(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkNoPermission(httpSession);
        model.addAttribute("signUpForm", new SignUpForm());
        return "/member/signUp";
    }

    /**
     * 회원가입 진행
     * 아이디, 비밀번호, 이메일 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     */
    @PostMapping("/member/signUp")
    public String signUp(@Valid SignUpForm signUpForm, BindingResult bindingResult) throws PDFLOException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            return "/member/signUp";
        }
        memberService.signUp(signUpForm);

        return "redirect:/";
    }

    /**
     * 로그인 폼 페이지로 넘겨준다.
     * 로그인 상태로 로그인을 다시 진행하려고 하면 에러.
     */
    @GetMapping("/member/login")
    public String loginForm(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkNoPermission(httpSession);
        model.addAttribute("loginForm", new LoginForm());
        return "/member/login";
    }

    /**
     * 로그인 진행
     * 아이디, 비밀번호 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     * 아이디, 비밀번호가 제대로 입력되었다면 세션에 로그인 상태 유지를 위한 Id 값을 추가
     * 메일 인증을 진행한 회원이라면 메인 페이지로
     * 메일 인증을 아직 진행하지 않은 회원이라면 메일 인증 페이지로
     * 프로필 사진이 있는 회원이라면 사진을 추가해서 메인페이지로 넘겨주자.
     */
    @PostMapping("/member/login")
    public String login(@Valid LoginForm loginForm, BindingResult bindingResult, HttpSession httpSession) throws PDFLOException, NoSuchAlgorithmException, MessagingException {
        if (bindingResult.hasErrors()) {
            return "/member/login";
        }

        Member member = memberService.login(loginForm);

        httpSession.setAttribute("id", member.getId());

        if (member.getType().equals(MemberType.UNAUTHORIZATION)) {
            int authCode = mailSender.sendingAuthMail(member.getEmail());
            httpSession.setAttribute("authCode", authCode);
            return "/member/authenticateForm";
        }

        httpSession.setAttribute("auth", true);
        Optional<Profile> findProfile = memberService.findProfileByMember(member.getId());

        if(!findProfile.isEmpty()){
            Profile profile = findProfile.get();
            httpSession.setAttribute("profile", profile.getFileInfo().getLocation() +
                    "/resized-" + profile.getFileInfo().getSaltedFileName() + profile.getFileInfo().getExtension());
        }
        return "redirect:/";
    }

    /**
     * 입력된 코드가 우리가 메일로 보낸 인증 코드와 동일하면 member를 인증회원으로 Update해준다.
     */
    @PostMapping("/member/authenticateMail")
    public String auth(HttpSession httpSession, int code) throws PDFLOException {
        Long memberId = (Long) httpSession.getAttribute("id");
        int authCode = (int) httpSession.getAttribute("authCode");

        if (authCode != code) {
            throw new PDFLOException(ErrorCode.MAIL_AUTHCODE_INCORRECT);
        }

        memberService.authorize(memberId);
        return "redirect:/";
    }

    /**
     * 로그인 상태라면 세션을 날려서 로그인 상태를 초기화해준다.
     */
    @GetMapping("/member/logout")
    public String logout(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        httpSession.invalidate();

        return "redirect:/";
    }

}
