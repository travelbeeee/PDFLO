package travelbeeee.PDFLO.domain.exception;

import lombok.Getter;

@Getter
public class PDFLOException extends Exception{
    ErrorCode returnCode;

    public PDFLOException(ErrorCode returnCode){
        this.returnCode = returnCode;
    }
}