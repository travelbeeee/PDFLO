package travelbeeee.PDFLO_V20.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import travelbeeee.PDFLO_V20.domain.enumType.FileType;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.HashMap;

@ControllerAdvice("travelbeeee.PDFLO_V20.controller")
public class GlobalControllerAdvice {

    private HashMap<ErrorCode,String> viewMap = new HashMap<ErrorCode,String>();

    @PostConstruct
    private void postConstruct(){
        viewMap.put(ErrorCode.MEMBER_NAME_DUPLICATION, "/member/signUp");
        viewMap.put(ErrorCode.LOGIN_INPUT_INVALID, "/member/login");
        viewMap.put(ErrorCode.SIGNUP_INPUT_INVALID, "/member/signUp");
        viewMap.put(ErrorCode.PASSWORD_INPUT_INVALID, "/member/login");
        viewMap.put(ErrorCode.COMMENT_NO_PERMISSION_BUYING, "/");
        viewMap.put(ErrorCode.COMMENT_ALREADY_WRITTEN, "/");
        viewMap.put(ErrorCode.COMMENT_NO_PERMISSION_WRITER, "/");
        viewMap.put(ErrorCode.COMMENT_INPUT_INVALID, "/");

        viewMap.put(ErrorCode.ITEM_ALREADY_BOUGHT, "/member/login");
        viewMap.put(ErrorCode.MEMBER_INSUFFICIENT_BALANCE, "/member/login");
        viewMap.put(ErrorCode.MEMBER_NO_PERMISSION, "/member/login");
        viewMap.put(ErrorCode.MEMBER_IS_SELLER, "/member/login");
        viewMap.put(ErrorCode.MAIL_AUTHCODE_INCORRECT, "/member/login");
        viewMap.put(ErrorCode.ITEM_INPUT_ERROR, "/member/login");
        viewMap.put(ErrorCode.MEMBER_ALREADY_LOGIN, "/member/login");
        viewMap.put(ErrorCode.PASSWORD_INPUT_INVALID, "/member/login");
        viewMap.put(ErrorCode.PASSWORD_INPUT_INVALID, "/member/login");
        viewMap.put(ErrorCode.PASSWORD_INPUT_INVALID, "/member/login");
        viewMap.put(ErrorCode.PASSWORD_INPUT_INVALID, "/member/login");
        viewMap.put(ErrorCode.PASSWORD_INPUT_INVALID, "/member/login");
    }

    @ExceptionHandler({travelbeeee.PDFLO_V20.exception.PDFLOException.class})
    public ModelAndView handleMyException(travelbeeee.PDFLO_V20.exception.PDFLOException e){
        ModelAndView mav = new ModelAndView();
        String viewName = viewMap.get(e.getReturnCode());
        mav.setViewName("redirect:?/" + viewName);
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
