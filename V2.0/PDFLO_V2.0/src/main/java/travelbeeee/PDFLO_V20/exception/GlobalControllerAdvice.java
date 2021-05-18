package travelbeeee.PDFLO_V20.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;

@ControllerAdvice("travelbeeee.PDFLO_V20.controller")
public class GlobalControllerAdvice {

    @ExceptionHandler({travelbeeee.PDFLO_V20.exception.PDFLOException.class})
    public ModelAndView handleMyException(travelbeeee.PDFLO_V20.exception.PDFLOException e){
        ModelAndView mav = new ModelAndView();
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
