//package travelbeeee.PDFLO_V20.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import travelbeeee.PDFLO_V20.utility.S3Uploader;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@RestController
//public class UploadTestController {
//
//    private final S3Uploader uploader;
//
//    @PostMapping("/api/v1/upload")
//    public String upload(@RequestParam("data") MultipartFile file) throws IOException {
//        return uploader.upload(file); // S3 bucket의 static/ 폴더를 지정한 것.
//    }
//}
