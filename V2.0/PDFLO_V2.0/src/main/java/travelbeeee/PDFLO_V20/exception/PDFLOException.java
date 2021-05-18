package travelbeeee.PDFLO_V20.exception;

import lombok.Getter;

@Getter
public class PDFLOException extends Exception{
    ReturnCode returnCode;

    public PDFLOException(ReturnCode returnCode){
        this.returnCode = returnCode;
    }
}