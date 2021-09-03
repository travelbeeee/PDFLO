package travelbeeee.PDFLO.web.controller.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import travelbeeee.PDFLO.domain.exception.PDFLOException;
import travelbeeee.PDFLO.domain.exception.ReturnCode;
import travelbeeee.PDFLO.domain.model.entity.Member;
import travelbeeee.PDFLO.domain.service.MemberService;
import travelbeeee.PDFLO.domain.utility.MailSender;
import travelbeeee.PDFLO.web.form.EmailForm;
import travelbeeee.PDFLO.web.form.UsernameForm;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberRestController {

    private final MemberService memberService;
    private final MailSender mailSender;

    /**
     * 이메일 input Form에서 가져온 이메일에 인증 코드 보내기.
     */
    @PostMapping("/sendMail")
    public ReturnCode sendAuthMail(@ModelAttribute @Valid EmailForm form, BindingResult bindingResult, HttpSession session) throws MessagingException {
        log.info("sendAuthMail 메소드 동작");
        if (bindingResult.hasErrors()) {
            return ReturnCode.MEMBER_EMAIL_INVALID;
        }
        String email = form.getEmail();
        int authCode = mailSender.sendingAuthMail(email);
        session.setAttribute("authCode", String.valueOf(authCode));

        return ReturnCode.SUCCESS;
    }

    /**
     * 입력된 코드가 우리가 메일로 보낸 인증 코드와 동일하면 member를 인증회원으로 Update해준다.
     */
    @PostMapping("/auth")
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
    @PostMapping("/duplicateCheck")
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
     * 확인한 비밀번호가 맞다면 회원을 삭제한다.
     */
    @DeleteMapping
    public ReturnCode memberDelete(HttpSession httpSession) throws PDFLOException, NoSuchAlgorithmException {
        if(httpSession.getAttribute("checkPassword") == null){
            throw new PDFLOException(ReturnCode.INVALID_ACCESS);
        }
        Long memberId = (Long) httpSession.getAttribute("id");
        ReturnCode result = memberService.delete(memberId);
        if (result == ReturnCode.SUCCESS) {
            httpSession.invalidate();
        }
        return result;
    }

    /**
     * 해당 회원의 프로필 파일 삭제하기.
     */
    @DeleteMapping("/profile")
    public ReturnCode deleteProfile(HttpSession httpSession) throws PDFLOException {
        log.info("deleteProfile 실행");
        Long memberId = (Long) httpSession.getAttribute("id");
        ReturnCode returnCode = memberService.deleteProfile(memberId);
        if(returnCode == ReturnCode.SUCCESS){
            httpSession.removeAttribute("profile");
        }
        return returnCode;
    }
}
