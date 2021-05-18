package travelbeeee.PDFLOpjt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import travelbeeee.PDFLOpjt.domain.Pdf;
import travelbeeee.PDFLOpjt.domain.Profile;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PdfRepositoryTest {
    @Autowired
    PdfRepository pdfRepository;

    @AfterEach
    void afterEach(){
        pdfRepository.deleteAll();
    }

    Pdf getPdf(){
        Pdf pdf = new Pdf();
        pdf.setContentId(1);
        pdf.setOriginFileName("origin");
        pdf.setSaltedFileName("salted");
        pdf.setLocation("location");
        return pdf;
    }

    @Test
    public void PDFinsert테스트() throws Exception{
        //given
        Pdf pdf = getPdf();

        //when
        int res = pdfRepository.insert(pdf);

        //then
        assertThat(res).isEqualTo(1);
    }

    @Test
    public void PDFdelete테스트() throws Exception{
        //given
        Pdf pdf = getPdf();
        //when
        pdfRepository.insert(pdf);
        int res = pdfRepository.delete(pdf.getPdfId());

        //then
        assertThat(res).isEqualTo(1);
    }
}