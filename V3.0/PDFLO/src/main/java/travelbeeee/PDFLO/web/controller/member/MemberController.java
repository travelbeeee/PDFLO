package travelbeeee.PDFLO.web.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Member;
import travelbeeee.PDFLO.domain.model.entity.Profile;
import travelbeeee.PDFLO.domain.service.MemberService;
import travelbeeee.PDFLO.domain.utility.MailSender;
import travelbeeee.PDFLO.web.form.EmailForm;
import travelbeeee.PDFLO.web.form.LoginForm;
import travelbeeee.PDFLO.web.form.SignUpForm;
import travelbeeee.PDFLO.web.form.UsernameForm;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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
        model.addAttribute("signUpForm", new SignUpForm());
        return "/member/signUp";
    }

    /**
     * 이메일 input Form에서 가져온 이메일에 인증 코드 보내기.
     */
    @ResponseBody
    @PostMapping("/member/sendMail")
    public ReturnCode sendAuthMail(@ModelAttribute @Valid EmailForm form, BindingResult bindingResult, HttpSession session) throws MessagingException {
        log.info("sendAuthMail 메소드 동작");
        if (bindingResult.hasErrors()) {
            return ReturnCode.MEMBER_EMAIL_INVALID;
        }
        String email = form.getEmail();
        int authCode = mailSender.sendingAuthMail(email);
        session.setAttribute("authCode", String.valueOf(authCode));
        log.info("email : {}", email);
        log.info("authCode : {}", authCode);
        return ReturnCode.SUCCESS;
    }

    /**
     * 입력된 코드가 우리가 메일로 보낸 인증 코드와 동일하면 member를 인증회원으로 Update해준다.
     */
    @ResponseBody
    @PostMapping("/member/auth")
    public Boolean auth(HttpSession httpSession, @RequestParam String inputCode, @RequestParam String email) throws PDFLOException {
        String authCode = (String) httpSession.getAttribute("authCode");
        if (authCode.equals(inputCode)) {
            httpSession.setAttribute("AUTHORIZATION", true);
            httpSession.setAttribute("email", email);
        }
        return authCode.equals(inputCode);
    }

    /**
     * 아이디 중복 확인
     */
    @ResponseBody
    @PostMapping("/member/duplicateCheck")
    public ReturnCode duplicateCheck(@ModelAttribute @Valid UsernameForm form, BindingResult bindingResult){
        log.info("duplicateCheck 메소드실행");
        if (bindingResult.hasErrors()) {
            return ReturnCode.MEMBER_NAME_INVALID;
        }
        Optional<Member> findMember = memberService.findMemberByUsername(form.getUsername());
        if(findMember.isEmpty())
            return ReturnCode.SUCCESS;
        return ReturnCode.MEMBER_NAME_DUPLICATION;
    }

    /**
     * 회원가입 진행
     * 아이디, 비밀번호, 이메일 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     */
    @PostMapping("/member/signUp")
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
    @GetMapping("/member/login")
    public String loginForm(HttpSession httpSession, Model model) throws PDFLOException {
        model.addAttribute("loginForm", new LoginForm());
        return "/member/login";
    }

    /**
     * 로그인 진행
     * 아이디, 비밀번호 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     * 아이디, 비밀번호가 제대로 입력되었다면 세션에 로그인 상태 유지를 위한 Id 값을 추가
     * 프로필 사진이 있는 회원이라면 사진을 추가해서 기존에 요청을 보냈던 페이지로 넘겨주자.
     */
    @PostMapping("/member/login")
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
                    "/resized-" + profile.getFileInfo().getSaltedFileName());
        }
        return "redirect:" + redirectURL;
    }


    /**
     * 로그인 상태라면 세션을 날려서 로그인 상태를 초기화해준다.
     */
    @GetMapping("/member/logout")
    public String logout(HttpSession httpSession) throws PDFLOException {
        httpSession.invalidate();

        return "redirect:/";
    }

}
