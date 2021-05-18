package travelbeeee.PDFLO_V20.exception;

import lombok.Getter;

@Getter
public class PDFLOException extends Exception{
    ErrorCode returnCode;

    public PDFLOException(ErrorCode returnCode){
        this.returnCode = returnCode;
    }
}