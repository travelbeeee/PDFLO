package travelbeeee.PDFLO_V20.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import travelbeeee.PDFLO_V20.domain.entity.Member;
import travelbeeee.PDFLO_V20.domain.enumType.MemberType;
import travelbeeee.PDFLO_V20.domain.form.LoginForm;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.service.MemberService;
import travelbeeee.PDFLO_V20.utility.MailSender;
import travelbeeee.PDFLO_V20.utility.PermissionChecker;
import travelbeeee.PDFLO_V20.domain.form.SignUpForm;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MailSender mailSender;
    /**
     *  회원가입 폼 페이지로 넘겨준다.
     *  로그인 상태로 회원가입을 진행하려고 하면 에러 발생.
     */
    @GetMapping("/member/signUp")
    public String signUpForm(HttpSession httpSession, Model model) throws PDFLOException {
        PermissionChecker.checkNoPermission(httpSession);
        model.addAttribute("signUpForm", new SignUpForm());
        return "/member/signUp";
    }

    /**
     *  회원가입 진행
     *  아이디, 비밀번호, 이메일 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     */
    @PostMapping("/member/signUp")
    public String signUp(@Valid SignUpForm signUpForm, BindingResult bindingResult) throws PDFLOException, NoSuchAlgorithmException {
        if(bindingResult.hasErrors()) {
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
     *  로그인 진행
     *  아이디, 비밀번호 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     *  아이디, 비밀번호가 제대로 입력되었다면 세션에 로그인 상태 유지를 위한 Id 값을 추가
     *  메일 인증을 진행한 회원이라면 메인 페이지로
     *  메일 인증을 아직 진행하지 않은 회원이라면 메일 인증 페이지로
     *  프로필 사진이 있는 회원이라면 사진을 추가해서 메인페이지로 넘겨주자.
     */
    @PostMapping("/member/login")
    public String login(@Valid LoginForm loginForm, BindingResult bindingResult, HttpSession httpSession) throws PDFLOException, NoSuchAlgorithmException, MessagingException {
        if(bindingResult.hasErrors()) {
            return "/member/login";
        }

        Member member = memberService.login(loginForm);

        httpSession.setAttribute("id", member.getId());

        if (member.getType().equals(MemberType.UNAUTHORIZATION)) {
            int authCode = mailSender.sendingAuthMail(member.getEmail());
            log.info("메일로보낸 authCode : " + authCode);
            httpSession.setAttribute("authCode", authCode);
            return "/member/authenticateForm";
        }

        httpSession.setAttribute("auth", true);
// 프로필은 Resize 개선 후 다시하자.
//        Optional<Profile> findProfile = memberService.findProfileByMember(member.getId());
//
//        if(!findProfile.isEmpty()){
//            Profile profile = findProfile.get();
//            httpSession.setAttribute("profile", profile.getFileInfo().getLocation() +
//                    "/resized-" + profile.getFileInfo().getSaltedFileName() + profile.getFileInfo().getExtension());
//        }
        return "redirect:/";
    }

    @PostMapping("/member/authenticateMail")
    public String auth(HttpSession httpSession, int code) throws PDFLOException {
        Long memberId = (Long)httpSession.getAttribute("id");
        int authCode = (int)httpSession.getAttribute("authCode");

        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.info("AuthCode : " + authCode);
        log.info("InputCode : " + code);

        if(authCode != code) {
            throw new PDFLOException(ErrorCode.MAIL_AUTHCODE_INCORRECT);
        }
        
        memberService.authorize(memberId);
        return "redirect:/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);
        httpSession.invalidate();

        return "redirect:/";
    }

    @PostMapping("/member/delete")
    public String userDelete(HttpSession httpSession) throws PDFLOException {
        PermissionChecker.checkPermission(httpSession);

        memberService.delete((Long) httpSession.getAttribute("userId"));
        httpSession.invalidate();

        return "redirect:/";
    }

}
