package travelbeeee.PDFLO.domain.exception;

import lombok.Getter;

@Getter
public class PDFLOException extends Exception{
    Code returnCode;

    public PDFLOException(Code returnCode){
        this.returnCode = returnCode;
    }
}