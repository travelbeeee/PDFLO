package travelbeeee.PDFLO.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.HashMap;


@Slf4j
@ControllerAdvice("travelbeeee.PDFLO.web.controller")
public class GlobalControllerAdvice {

    private HashMap<ReturnCode,String> redirectMap = new HashMap<ReturnCode,String>();

    @PostConstruct
    private void postConstruct(){
        redirectMap.put(ReturnCode.MEMBER_NO_EXIST, "/member/signUp");
        redirectMap.put(ReturnCode.MEMBER_NOT_SELLER, "/");
        redirectMap.put(ReturnCode.MEMBER_IS_SELLER, "/");
        redirectMap.put(ReturnCode.MEMBER_NAME_DUPLICATION, "/member/signUp");
        redirectMap.put(ReturnCode.MEMBER_INSUFFICIENT_BALANCE, "/member/mypage");
        redirectMap.put(ReturnCode.MEMBER_NO_PERMISSION, "/member/login");
        redirectMap.put(ReturnCode.MEMBER_ALREADY_LOGIN, "/");
        redirectMap.put(ReturnCode.LOGIN_INPUT_INVALID, "/member/login");
        redirectMap.put(ReturnCode.SIGNUP_INPUT_INVALID, "/member/signUp");
        redirectMap.put(ReturnCode.PASSWORD_INPUT_INVALID, "/member/login");

        redirectMap.put(ReturnCode.COMMENT_NO_EXIST, "/");
        redirectMap.put(ReturnCode.COMMENT_NO_PERMISSION_BUYING, "/");
        redirectMap.put(ReturnCode.COMMENT_ALREADY_WRITTEN, "/");
        redirectMap.put(ReturnCode.COMMENT_NO_PERMISSION_WRITER, "/");
        redirectMap.put(ReturnCode.COMMENT_INPUT_INVALID, "/");

        redirectMap.put(ReturnCode.ITEM_NO_EXIST, "/");
        redirectMap.put(ReturnCode.ITEM_ALREADY_BOUGHT, "/");
        redirectMap.put(ReturnCode.ITEM_INPUT_ERROR, "/item/upload");

        redirectMap.put(ReturnCode.MAIL_AUTHCODE_INCORRECT, "/member/login");
        redirectMap.put(ReturnCode.DOWNLOAD_NO_PERMISSION, "/");
        redirectMap.put(ReturnCode.PROFILE_NO_EXIST, "/member/mypage");
        redirectMap.put(ReturnCode.CART_NO_EXIST, "/");
        redirectMap.put(ReturnCode.CART_ALREADY_EXIST, "/");
    }

    @ExceptionHandler({travelbeeee.PDFLO.domain.exception.PDFLOException.class})
    public ModelAndView handleMyException(travelbeeee.PDFLO.domain.exception.PDFLOException e){
        ModelAndView mav = new ModelAndView();
        String redirectURL = redirectMap.get(e.getReturnCode());
        mav.setViewName("/errors/error");
        mav.addObject("errorCode", e.getReturnCode().getMessage());
        mav.addObject("redirectURL", redirectURL);
        return mav;
    }
}