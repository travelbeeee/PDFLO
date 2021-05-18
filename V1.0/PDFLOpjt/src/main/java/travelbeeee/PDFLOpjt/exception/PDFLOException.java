package travelbeeee.PDFLOpjt.exception;

import lombok.Getter;
import travelbeeee.PDFLOpjt.utility.ReturnCode;

@Getter
public class PDFLOException extends Exception{
    ReturnCode returnCode;

    public PDFLOException(ReturnCode returnCode){
        this.returnCode = returnCode;
    }
}