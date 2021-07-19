package travelbeeee.PDFLO.domain.utility;

import travelbeeee.PDFLO.domain.exception.ErrorCode;
import travelbeeee.PDFLO.domain.exception.PDFLOException;

import javax.servlet.http.HttpSession;

public class PermissionChecker {
    public static void checkPermission(HttpSession httpSession) throws PDFLOException {
        if(httpSession.getAttribute("id") == null) throw new PDFLOException(ErrorCode.MEMBER_NO_PERMISSION);
        if(httpSession.getAttribute("auth") == null) throw new PDFLOException(ErrorCode.MEMBER_NO_PERMISSION);
    }

    public static void checkNoPermission(HttpSession httpSession) throws PDFLOException {
        if(httpSession.getAttribute("auth") != null) throw new PDFLOException(ErrorCode.MEMBER_ALREADY_LOGIN);
    }
}
