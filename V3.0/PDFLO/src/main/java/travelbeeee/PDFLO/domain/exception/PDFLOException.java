package travelbeeee.PDFLO.domain.exception;

import lombok.Getter;

@Getter
public class PDFLOException extends Exception{
    ReturnCode returnCode;

    public PDFLOException(ReturnCode returnCode){
        this.returnCode = returnCode;
    }
}