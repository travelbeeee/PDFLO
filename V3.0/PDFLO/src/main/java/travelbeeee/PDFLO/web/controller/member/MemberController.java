package travelbeeee.PDFLO.web.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import travelbeeee.PDFLO.domain.exception.ErrorCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.model.entity.Member;
import travelbeeee.PDFLO.domain.model.entity.Profile;
import travelbeeee.PDFLO.domain.model.enumType.MemberType;
import travelbeeee.PDFLO.domain.service.MemberService;
import travelbeeee.PDFLO.domain.utility.MailSender;
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
    @PostMapping("/member/sendMail")
    public ResponseEntity<?> sendAuthMail(@RequestParam String email, HttpSession session) throws MessagingException {
        log.info("email : {}", email);
        int authCode = mailSender.sendingAuthMail(email);
        log.info("authCode : {}", authCode);
        session.setAttribute("authCode", String.valueOf(authCode));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 입력된 코드가 우리가 메일로 보낸 인증 코드와 동일하면 member를 인증회원으로 Update해준다.
     */
    @ResponseBody
    @PostMapping("/member/auth")
    public Boolean auth(HttpSession httpSession, @RequestParam String inputCode) throws PDFLOException {
        String authCode = (String) httpSession.getAttribute("authCode");
        if (authCode.equals(inputCode)) {
            httpSession.setAttribute("AUTHORIZATION", true);
        }
        return authCode.equals(inputCode);
    }

    /**
     * 아이디 중복 확인
     */
    @ResponseBody
    @PostMapping("/member/duplicateCheck")
    public Boolean duplicateCheck(@ModelAttribute @Valid UsernameForm form, BindingResult bindingResult){
        log.info("duplicateCheck 메소드실행");
        if (bindingResult.hasErrors()) {
            return false;
        }
        log.info("username : {}", form.getUsername());
        Optional<Member> findMember = memberService.findMemberByUsername(form.getUsername());
        log.info("findMember : {}", findMember);
        if(findMember.isEmpty())
            return true;
        return false;
    }

    /**
     * 회원가입 진행
     * 아이디, 비밀번호, 이메일 입력 오류시 다시 회원가입 폼 페이지로 보내기.
     */
    @PostMapping("/member/signUp")
    public String signUp(@Valid SignUpForm signUpForm, BindingResult bindingResult, HttpServletRequest request) throws PDFLOException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()){
            return "/member/signUp";
        }
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("AUTHORIZATION") == null){ // 메일 인증 안했음!
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
            throw new PDFLOException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        Member member = memberService.login(loginForm);

        httpSession.setAttribute("id", member.getId());

        Optional<Profile> findProfile = memberService.findProfileByMember(member.getId());

        if(!findProfile.isEmpty()){
            Profile profile = findProfile.get();
            httpSession.setAttribute("profile", profile.getFileInfo().getLocation() +
                    "/resized-" + profile.getFileInfo().getSaltedFileName() + profile.getFileInfo().getExtension());
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
