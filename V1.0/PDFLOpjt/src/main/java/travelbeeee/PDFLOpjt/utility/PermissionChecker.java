package travelbeeee.PDFLOpjt.utility;

import travelbeeee.PDFLOpjt.exception.PDFLOException;

import javax.servlet.http.HttpSession;

public class PermissionChecker {

    public static void checkPermission(HttpSession httpSession) throws PDFLOException {
        if(httpSession.getAttribute("userId") == null) throw new PDFLOException(ReturnCode.USER_NO_PERMISSION);
    }

    public static void checkNoPermission(HttpSession httpSession) throws PDFLOException {
        if(httpSession.getAttribute("userId") != null) throw new PDFLOException(ReturnCode.USER_ALREADY_LOGIN);
    }
}
