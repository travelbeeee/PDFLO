package travelbeeee.PDFLO.domain.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.HashMap;

@ControllerAdvice("travelbeeee.PDFLO_V20.controller")
public class GlobalControllerAdvice {

    private HashMap<Code,String> viewMap = new HashMap<Code,String>();

    @PostConstruct
    private void postConstruct(){
        viewMap.put(Code.MEMBER_NAME_DUPLICATION, "/member/signUp");
        viewMap.put(Code.LOGIN_INPUT_INVALID, "/member/login");
        viewMap.put(Code.SIGNUP_INPUT_INVALID, "/member/signUp");
        viewMap.put(Code.PASSWORD_INPUT_INVALID, "/member/login");
        viewMap.put(Code.COMMENT_NO_PERMISSION_BUYING, "/");
        viewMap.put(Code.COMMENT_ALREADY_WRITTEN, "/");
        viewMap.put(Code.COMMENT_NO_PERMISSION_WRITER, "/");
        viewMap.put(Code.COMMENT_INPUT_INVALID, "/");
        viewMap.put(Code.ITEM_ALREADY_BOUGHT, "/");
        viewMap.put(Code.MEMBER_INSUFFICIENT_BALANCE, "/");
        viewMap.put(Code.MEMBER_NO_PERMISSION, "/member/login");
        viewMap.put(Code.MEMBER_IS_SELLER, "/");
        viewMap.put(Code.MAIL_AUTHCODE_INCORRECT, "/member/login");
        viewMap.put(Code.ITEM_INPUT_ERROR, "/item/upload");
        viewMap.put(Code.MEMBER_ALREADY_LOGIN, "/");
        viewMap.put(Code.MEMBER_NOT_SELLER, "/");
        viewMap.put(Code.DOWNLOAD_NO_PERMISSION, "/");
        viewMap.put(Code.MEMBER_NO_EXIST, "/member/signUp");
        viewMap.put(Code.ITEM_NO_EXIST, "/");
        viewMap.put(Code.PROFILE_NO_EXIST, "/member/mypage");
        viewMap.put(Code.COMMENT_NO_EXIST, "/");
        viewMap.put(Code.CART_NO_EXIST, "/");
        viewMap.put(Code.CART_ALREADY_EXIST, "/");
    }

    @ExceptionHandler({travelbeeee.PDFLO.domain.exception.PDFLOException.class})
    public ModelAndView handleMyException(travelbeeee.PDFLO.domain.exception.PDFLOException e){
        ModelAndView mav = new ModelAndView();
        String viewName = viewMap.get(e.getReturnCode());
        mav.setViewName("/errors/error");
        mav.addObject("errorCode", e.getReturnCode().getMessage());
        return mav;
    }

    @ExceptionHandler({MessagingException.class})
    public ModelAndView handleMessagingException(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/errors/error");
        mav.addObject("errorCode", "인증 메일을 보내는데 실패했습니다. 다시 시도해주세요.");
        return mav;
    }
}
