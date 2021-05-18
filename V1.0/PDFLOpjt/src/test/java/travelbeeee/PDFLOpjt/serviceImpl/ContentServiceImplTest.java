package travelbeeee.PDFLOpjt.serviceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLOpjt.inputform.ContentForm;
import travelbeeee.PDFLOpjt.service.ContentService;
import travelbeeee.PDFLOpjt.utility.FileManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class ContentServiceImplTest {
    @Autowired
    ContentService contentService;
    @Autowired
    FileManager fileService;
    @Autowired

    MultipartFile getMultipartFile(){
        Path path = Paths.get("C:\\Users\\HyunSeok\\Desktop\\studyWithMe\\gitHub\\img\\20210114_124921.png");
        String name = "20210114_124921.png";
        String originFileName = "20210114_124921.png";
        String contentType = "image/png";
        byte[] content = null;
        try{
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new MockMultipartFile(name, originFileName, contentType, content);
        return multipartFile;
    }

    ContentForm getWritingForm(){
        ContentForm contentForm = new ContentForm();
        contentForm.setTitle("title");
        contentForm.setContent("content");
        contentForm.setPrice(1000);
        contentForm.setPdfFile(getMultipartFile());
        contentForm.setThumbnailFile(getMultipartFile());

        return contentForm;
    }

//    @AfterEach
//    void afterEach(){
//        List<Content> contents = contentService.selectAll();
//        for(Content content : contents){
//
//        }
//    }
    @Test
    public void 글쓰기테스트() throws Exception{
        //given
        ContentForm contentForm = getWritingForm();
        //when
//        contentService.writing(contentForm, 1);
        //then
        // 실제 DB 와 로컬폴더에 데이터가 생기고 파일이 생기는지 확인!! --> OK!!
    }

    @Test // PDF 파일만 수정된 경우!
    public void PDF파일만수정() throws Exception{
        //given 45번 content를 미리 넣어놓음!
        //when

        //then

    }

    @Test // 썸네일 파일만 수정된 경우!
    public void 썸네일파일만수정() throws Exception{
        //given

        //when

        //then

    }

    @Test // 제목 내용만 수정된 경우!
    public void 제목만수정() throws Exception{
        //given

        //when

        //then

    }
}