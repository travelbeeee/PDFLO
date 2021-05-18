package travelbeeee.PDFLOpjt.repository;

import org.apache.ibatis.annotations.Mapper;
import travelbeeee.PDFLOpjt.domain.Pdf;

@Mapper
public interface PdfRepository {
    int insert(Pdf pdf);
    int updateContentId(Pdf pdf);
    int delete(int pdfId);
    int deleteAll();
    Pdf selectById(int pdfId);
}
